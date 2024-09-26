<template>
    <el-form :model="form" label-width="auto" style="max-width: 100%" :rules="formrules" ref="formref">
        <el-form-item label="入库单号">
            <el-input v-model="form.ibno" disabled />
        </el-form-item>
        <el-form-item label="供应商" prop="sup">
            <el-select v-model="form.sup" placeholder="请先选择供应商" @change="clearCpTable">
                <el-option v-for="item in sups" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
        </el-form-item>
        <el-form-item label="备注">
            <el-input v-model="form.note" type="textarea" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item>
            <el-divider></el-divider>
        </el-form-item>
        <el-form-item  class="cpDetail"  >
            <span style="width: 100px;
      font-weight: bold;
      font-size: 24px;">零件明细</span>
            <el-button @click="addCp" type="primary" style="margin-left: 20px;">添加零件</el-button>
            <el-button @click="dlCp">删除零件</el-button>
        </el-form-item>
        <el-form-item >
            <el-table :data="cpTable" style="width: 100%" :cell-style="{ textAlign: 'center' }"
                :header-cell-style="{ textAlign: 'center' }" @selection-change="sCpChg" height="400px">
                <el-table-column type="selection" />
                <el-table-column prop="cpno" label="零件号" width="180" />
                <el-table-column prop="innum" label="入库数量" width="180">
                    <template #default="scope">
                        <el-input-number v-model="scope.row.innum" :min=1 @change="calcBox(scope.row)">
                        </el-input-number>
                    </template>
                </el-table-column>
                <el-table-column prop="boxnum" label="箱数" width="180" />
                <el-table-column prop="cpco" label="零件编号" width="180" />
                <el-table-column prop="rpvo" label="包装容量" width="180">
                    <template #default="scope">
                        <el-input-number v-model="scope.row.rpvo" :min=1 @change="calcBox(scope.row)">
                        </el-input-number>
                    </template>
                </el-table-column>
            </el-table>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit(formref)">提交</el-button>
            <!-- <el-button>取消</el-button> -->
        </el-form-item>
    </el-form>
    <el-dialog v-model="diavis" width="650">
        <el-form :model="form">
            <el-form-item label="供应商">
                <el-input v-model="form.sup" disabled></el-input>
            </el-form-item>
            <el-form-item label="零件号">
                <el-input v-model="slctno" placeholder="请输入零件号"></el-input>
            </el-form-item>
            <el-form-item >
                <el-button @click="screenCp" type="primary" style="right: 0%;position: absolute;">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-table :data="cpBTable" style="width: 100%;" :cell-style="{ textAlign: 'center' }"
                :header-cell-style="{ textAlign: 'center' }" @selection-change="sCpBChg" height="400px">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="name" label="零件号" width="180px"></el-table-column>
                    <el-table-column prop="no" label="零件编号" width="180px"></el-table-column>
                    <el-table-column prop="num" label="数量" width="180">
                    <template #default="scope">
                        <el-input-number v-model="scope.row.num" :min=1 @change="slnumchange(scope.row)">
                        </el-input-number>
                    </template>
                </el-table-column>
                </el-table>
            </el-form-item>
            <!-- <el-form-item>
                <el-input-number v-model="slctnum" placeholder="数量" :min=1></el-input-number>
            </el-form-item> -->
            <el-form-item>
                <el-button type="primary" @click="submitAdd">添加</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    <el-dialog v-model="errDia">
        <span>未添加零件！</span>
    </el-dialog>
</template>

<script lang="ts" setup>
import { FormRules, FormInstance } from 'element-plus';
import { Ref, reactive, ref } from 'vue'
import axios from 'axios';
const props=defineProps({
    closeNav:{
        type:Function,
        required:true
    }
})
let url='http://localhost:8080/'
let formref = ref<FormInstance>()
// do not use same name with ref
const diavis = ref(false)
const errDia =ref(false)
interface Cp {
    cpno: string
    innum: number
    boxnum: number
    cpco: string
    rpvo: number
}
interface Cpb {
    name: string
    no: string
    sup: string
    num:number
}
let cpTable: Ref<Cp[]> = ref([])
let sCp: Ref<Cp[]> = ref([

])
let cpBTable: Ref<Cpb[]> = ref([
])
let sCpB: Ref<Cpb[]> = ref([])
let cpBL: Ref<Cpb[]> = ref([])
interface Form {
    ibno: string
    sup: string
    note: string
    cpData: Cp[]
}
let form = reactive<Form>({
    ibno: '',
    sup: '',
    note: '',
    cpData: [],
})
form.ibno = getNowFormatDate()
let sups:Ref<{name:string}[]> = ref([])

let Cps: Ref<Cpb[]> = ref([])

axios.get(url+'item').then(function({data}){
    for(let cp of data){
        Cps.value.push({
            name:cp.itemNum,
            no:cp.itemSerial,
            sup:cp.supplier,
            num:1
        })
       }
       for(let cp of Cps.value){
    let flag=true
    for(let sup of sups.value){
        if(sup.name==cp.sup){
            flag=false
        }
    }
    if(flag)
    sups.value.push({name:cp.sup})
}
    console.log(data)
})
let slctno = ref('')
let slctnum = ref(1)
var myindexof = function (arr, v) {
    for (var i = 0; i < arr.length; i++) {
        if (JSON.stringify(arr[i]) == JSON.stringify(v))
            return i;
    }
    return -1;
}
const onSubmit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            form.cpData=cpTable.value
            if(form.cpData.length>0){
               let pCps:({
                    itemNum:string
                    packagingCapacity:Number
                    planQuantity:Number
                }[])=[]
                for(let cp of form.cpData){
                    pCps.push({
                        itemNum:cp.cpno,
                        packagingCapacity:cp.rpvo,
                        planQuantity:cp.innum
                    })
                }
                let  data={
                    "inboundNum":form.ibno,
                    "supplier":form.sup,
                    "inboundDetail":pCps
                }
                axios.post(url+'inbound',data)
                 props.closeNav({title:"创建入库单",path:"/createInbound"})
              console.log('submit!')
            }
            else{
                console.log('error submit!')
                errDia.value=true
            }
        } else {
            console.log('error submit!')
        }   
    })
}
const sCpChg = (val: Cp[]) => {
    sCp.value = val

}
const sCpBChg = (val: Cpb[]) => {
    sCpB.value = val
}
const dlCp = () => {
    cpTable.value = cpTable.value.filter(function (v) { return myindexof(sCp.value, v) == -1 })
}
const clearCpTable = () => {
    cpTable.value = []
}
const screenCp = () => {
    let regex = new RegExp(slctno.value, 'u')
    cpBTable.value = cpBL.value.filter(function (v) { return regex.test(v.name) })
}
const addCp = () => {
    cpBL.value = Cps.value.filter(function (v) { return v.sup == form.sup })
    for(let tmp of cpBL.value){
        tmp.num=1
    }
    cpBTable.value = cpBL.value

    diavis.value = true
}
const calcBox = (row) => {
    if (row.innum == null) row.innum = 1
    if (row.rpvo == null) row.rpvo = 1
    row.boxnum = Math.ceil(row.innum / row.rpvo)
}
const slnumchange=(row)=>{
    if(row.num==null) row.num=1
}
const submitAdd = () => {
    let addarr: Cp[] = []
    for (let i = 0; i < sCpB.value.length; i++) {
        let temp: Cp = {
            cpno: sCpB.value[i].name,
            innum: sCpB.value[i].num == null ? 1 : sCpB.value[i].num,
            boxnum: sCpB.value[i].num == null ? 1 : sCpB.value[i].num,
            cpco: sCpB.value[i].no,
            rpvo: 1
        }
        addarr.push(temp)
    }
    cpTable.value = cpTable.value.concat(addarr)
}
const formrules = reactive<FormRules>({
    sup: [
        { required: true, message: "请选择供应商", trigger: 'blur' }
    ],
})

//获取当前日期函数
function getNowFormatDate() {
    let date = new Date(),
        obj = {
            year: date.getFullYear(), //获取完整的年份(4位)
            month: date.getMonth() + 1, //获取当前月份(0-11,0代表1月)
            strDate: date.getDate(), // 获取当前日(1-31)
            week: '星期' + '日一二三四五六'.charAt(date.getDay()), //获取当前星期几(0 ~ 6,0代表星期天)
            hour: date.getHours(), //获取当前小时(0 ~ 23)
            minute: date.getMinutes(), //获取当前分钟(0 ~ 59)
            second: date.getSeconds() //获取当前秒数(0 ~ 59)
        }

    Object.keys(obj).forEach(key => {
        if (obj[key] < 10) obj[key] = `0${obj[key]}`
        // console.log(obj[key])
    })

    return `R-${obj.year}-${obj.month}-${obj.strDate}-${obj.hour}-${obj.minute}-${obj.second}`
}


</script>