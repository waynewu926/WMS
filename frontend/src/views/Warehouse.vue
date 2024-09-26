<template>
  <!-- 搜索栏 -->
  <el-form
  ref="ruleFormRef"
  style="max-width: 600px"
  :model="ruleForm"
  label-width="auto"
  class="demo-ruleForm"
  :size="formSize"
  status-icon
>
  <el-form-item label="入库状态" prop="inboundStatus">
    <el-segmented v-model="ruleForm.inboundStatus" :options="stateOptions" />
  </el-form-item>
  <el-form-item label="入库单号" prop="inboundNum">
    <el-input v-model="ruleForm.inboundNum" />
  </el-form-item>
  <el-form-item label="供应商" prop="supplier">
    <el-select v-model="ruleForm.supplier" placeholder="供应商">
      <el-option label='全部' value='' />
      <!-- <el-option label="1WT" value="1WT" />
      <el-option label="2GC" value="2GC" />
      <el-option label="3JG" value="3JG" />
      <el-option label="4WY" value="4WY" /> -->
      <el-option
        v-for="item in supplierList"
        :key="item.id"
        :label="item.supplierCode"
        :value="item.supplierCode"
      />
    </el-select>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click.prevent = "searchInbound">
      查询
    </el-button>
    <el-button @click="resetForm">重置</el-button>
  </el-form-item>
</el-form>  

<!-- 创建入库单入口 -->

<el-button type="primary" color="	#6495ED" size="large" @click="createIb">+创建入库单</el-button>

  <!-- 列表 -->
  <el-table :data="tableData" v-loading="loading" row-key="id"    height="500" style="width: 100%"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="inboundNum" label="入库单号" width="300" />
      <el-table-column prop="supplier" label="供应商" width="250" />
      <el-table-column prop="inboundStatus" label="入库状态" width="250">
        <template v-slot:default="scope">
          <el-tag :type=getTag(scope.row.inboundStatus)>{{ getStatus(scope.row.inboundStatus) }}</el-tag>            
        </template>
      </el-table-column>
      <el-table-column  label="备注" />
      <el-table-column fixed="right" label="操作" width="200">
          <template #default="scope">
              <!-- <el-button
              link
              type="primary"
              size="small"
              @click.prevent="deleteRow(scope.$index)"
              >
              修改
              </el-button> -->
              <!-- 按钮示例 -->
              <el-button
              link
              type="primary"
              size="small"
              @click.prevent="inbound(scope.row.inboundNum)"
              >
              入库
              </el-button>
              <el-button
              link
              type="primary"
              size="small"
              @click.prevent="printInbound(scope.$index)"
              >
              打印入库单
              </el-button>
              <el-button
              link
              type="primary"
              size="small"
              @click.prevent="deleteIb(scope.row.id)"
              >
              删除
              </el-button>
    </template>
  </el-table-column>
  </el-table>

</template>

<script>
import { onMounted ,ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
export default {
  name: "Warehouse",
  inject: ['reload'], 
  props:['curMenu'],
  setup() {
      const stateOptions = ['全部', '未入库', '部分入库','已入库']
      const ruleForm = ref({
      inboundStatus : '全部',
      inboundNum: null,
      supplier: null
      })
     
      const loading=ref(false)
      const router = useRouter();
      const tableData = ref([])
      const supplierList = ref([])

      onMounted(async () => {
        //获取入库单数据
        try {
          const response = await axios.get('http://localhost:8080/inbound'); // 发起 Axios GET 请求
          tableData.value = response.data; // 将响应数据赋给 tableData
        } catch (error) {
          console.error('Error fetching data:', error);
        }
        //获取供应商数据
        try {
          const response = await axios.get('http://localhost:8080/supplier');
          supplierList.value = response.data; 
        } catch (error) {
          console.error('Failed to fetch supplier data', error);
        }
     });

    //  //测试用数组
    // const tableData=ref([
    //   {
    //     id:'1',
    //     inboundStatus:0,
    //     inboundNum:'12348-66544',
    //     supplier:'A'
    //   },
    //   {
    //     id:'2',
    //     inboundStatus:0,
    //     inboundNum:'18998-66544',
    //     supplier:'B'
    //   },
    //   {
    //     id:'3',
    //     inboundStatus:1,
    //     inboundNum:'1296633-66544',
    //     supplier:'C'
    //   }
    // ])
    // const supplierList = ref([
    //     {
    //       id:'1',
    //       supplierCode:'1WT',
    //       supplierName:'wgc'
    //     },
    //     {
    //       id:'2',
    //       supplierCode:'2WT',
    //       supplierName:'wgc'
    //     },
    //     {
    //       id:'1',
    //       supplierCode:'2WT',
    //       supplierName:'wgc'
    //     }
    //   ])

     function convertStatus(state){
      switch(state){
        case '全部':
          return null;
        case '已入库':
          return 0;
        case '部分入库':
          return 1;
        case '未入库':
          return 2;
        default:
          return '';
      }
     }

     function getStatus(state){
      console.log(state)
      switch(state){
        case 0:
          return '已入库';
        case 1:
          return '部分入库';
        case 2:
          return '未入库';
        default:
          return '未入库';
      }
     }

     function getTag(state){
      switch(state){
        case 0:
          return "primary";
        case 1:
          return "success";
        case 2:
          return "info";
        default:
          return "info";
      }
     }
     const resetForm = ()=>{
      // 清空表单数据
      ruleForm.value = {
        inboundStatus: '全部', // 入库状态字段
        inboundNum: null,    // 入库单号字段
        supplier: null,      // 供应商字段
      };
    }

    const getSupplierData = async() =>{
        try {
          const response = await axios.get('http://localhost:8080/supplier');
          supplierList.value = response.data; // 假设返回的数据格式是 [{ label: '1WT', value: '1WT' }, ...]
        } catch (error) {
          console.error('Failed to fetch supplier data', error);
        }
      }

      const getIb = async () => {
        try {
          console.log('调用了getIb');
          loading.value = true;
          const response = await axios.get('http://localhost:8080/inbound'); // 发起 Axios GET 请求
          tableData.value = response.data; // 将响应数据赋给 tableData
          loading.value = false
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      }

      const deleteIb = async (id) =>{
        console.log(id);
        ElMessageBox.confirm(
          '确定删除吗？', 
          '提示', 
          { 
            confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' 
          }).then(async () => { 
            // 用户点击确定后的操作，比如调用删除接口等 
                await axios.delete('http://localhost:8080/inbound/' + id);
               console.log(`Deleted inbound with id: ${id}`);
               try {
                console.log('调用了getIb');
                loading.value = true;
                const response = await axios.get('http://localhost:8080/inbound'); // 发起 Axios GET 请求
                tableData.value = [...response.data]; // 将响应数据赋给 tableData
                loading.value = false
        } catch (error) {
          console.error('Error fetching data:', error);
        }
          }).catch((error) => { 
             console.error('用户取消了操作:', error);
              });
        
      }

      const searchInbound = async () => {
        try {
          const response = await axios.get('http://localhost:8080/inbound', {
            params: {
              inboundStatus: convertStatus(ruleForm.value.inboundStatus),
              inboundNum: ruleForm.value.inboundNum,
              supplier: ruleForm.value.supplier
            }
          });
          tableData.value = [...response.data]; // 更新 tableData 的引用
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };

      const createIb = () =>{
        router.push("/createInbound");
      }     
      

      // const inbound = (inboundNum)=>{
      //   router.push("/inbound?inboundNum="+inboundNum)
      // }
      
      const inbound = (inboundNum)=>{
          router.push({
          path: `/inbound/${inboundNum}`
        });
          console.log(router.currentRoute.value)
      }

      const appendTr = function(table,detail) {
        let head = document.createElement('tr')
        let name = document.createElement('th')
        name.innerHTML = '物料名'
        let num = document.createElement('th')
        num.innerHTML = '物料编号'
        let plan = document.createElement('th')
        plan.innerHTML = '包装容量'
        let real = document.createElement('th')
        real.innerHTML = '数量'

        name.style.border = "2px solid #000"
        name.style.textAlign = "center"
        name.style.fontSize = "16px"
        name.style.fontWeight = "bold"
        num.style.border = "2px solid #000"
        num.style.textAlign = "center"
        num.style.fontSize = "16px"
        num.style.fontWeight = "bold"
        plan.style.border = "2px solid #000"
        plan.style.textAlign = "center"
        plan.style.fontSize = "16px"
        plan.style.fontWeight = "bold"
        real.style.border = "2px solid #000"
        real.style.textAlign = "center"
        real.style.fontSize = "16px"
        real.style.fontWeight = "bold"

        head.appendChild(name)
        head.appendChild(num)
        head.appendChild(plan)
        head.appendChild(real)

        table.appendChild(head)

        detail.data.forEach(el => {
          let tr = document.createElement('tr');

          let name = document.createElement('td');
          name.innerHTML = `${el.itemNum}`
          name.style.border = "2px solid #000"
          name.style.textAlign = "center"
          name.style.fontSize = "16px"

          let num = document.createElement('td');
          num.innerHTML = `${el.id}`
          num.style.border = "2px solid #000"
          num.style.textAlign = "center"
          num.style.fontSize = "16px"

          let plan = document.createElement('td');
          plan.innerHTML = `${el.planQuantity}`
          plan.style.border = "2px solid #000"
          plan.style.textAlign = "center"
          plan.style.fontSize = "16px"

          let real = document.createElement('td');
          real.innerHTML = `${el.realQuantity}`
          real.style.border = "2px solid #000"
          real.style.textAlign = "center"
          real.style.fontSize = "16px"

          tr.appendChild(name)
          tr.appendChild(num)
          tr.appendChild(plan)
          tr.appendChild(real)

          table.appendChild(tr)
        })
      }

      const printInbound = async function(index) {
          let supplier = supplierList.value[index];
          let inboundNum = tableData.value[index].inboundNum
          let currentDate = new Date()
          let inboundDetail = await axios.get(`http://localhost:8080/inboundDetail/${inboundNum}`);
          console.log('准备创建新界面')
          console.log('获取到的数据为',inboundDetail)

          let newWindow = window.open('',`入库单--${supplier.supplierName}`,null)

          let container = document.createElement('div');
          container.style.width = "40%"
          container.style.height =  "60vh"
          container.style.display = "flex"
          container.style.justifyContent = "center"
          container.style.alignItems = "center"
          container.style.flexDirection = "column"
          container.style.margin = "auto"
          container.classList.add('container')

          let headArea = document.createElement('div');
          headArea.style.width = "100%"
          headArea.style.marginBottom = "60px"
          headArea.classList.add('headArea')
          container.appendChild(headArea)

          let title = document.createElement('div');
          title.innerHTML = `入库单--${supplier.supplierName}`
          title.classList.add('title')
          title.style.margin = "0 auto"
          title.style.fontWeight = "bold"
          title.style.fontSize = "32px"
          title.style.width = "fit-content"
          headArea.appendChild(title)

          let tableHead = document.createElement('div');
          tableHead.classList.add('tableHead')
          tableHead.style.width = "100%"
          tableHead.style.height = "64px"
          tableHead.style.position = "relative"
          let supplierNode = document.createElement('div')
          let inboundNumNode = document.createElement('div')
          let typeNode = document.createElement('div')
          let dateNode = document.createElement('div')

          supplierNode.classList.add('left-top','innerTitle')
          supplierNode.innerHTML = `供应商: ${supplier.supplierName}`
          supplierNode.style.fontSize = "16px"
          supplierNode.style.padding = "8px"
          supplierNode.style.position = "absolute"
          supplierNode.style.left = "0"
          supplierNode.style.top = "0"

          typeNode.classList.add('left-down','innerTitle')
          typeNode.innerHTML = '入库方式: 采购入库'
          typeNode.style.fontSize = "16px"
          typeNode.style.padding = "8px"
          typeNode.style.position = "absolute"
          typeNode.style.left = "0"
          typeNode.style.top = "50%"

          inboundNumNode.classList.add('right-top','innerTitle')
          inboundNumNode.innerHTML = `入库单编号: ${inboundNum}`
          inboundNumNode.style.fontSize = "16px"
          inboundNumNode.style.padding = "8px"
          inboundNumNode.style.position = "absolute"
          inboundNumNode.style.left = "50%"
          inboundNumNode.style.top = "0"

          dateNode.classList.add('right-down','innerTitle')
          dateNode.innerHTML = `时间: ${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}`+' '+`${currentDate.getHours()}`+':'+`${currentDate.getMinutes()}`+':'+`${currentDate.getSeconds()}`
          dateNode.style.fontSize = "16px"
          dateNode.style.padding = "8px"
          dateNode.style.position = "absolute"
          dateNode.style.left = "50%"
          dateNode.style.top = "50%"

          container.appendChild(tableHead)
          tableHead.appendChild(supplierNode)
          tableHead.appendChild(inboundNumNode)
          tableHead.appendChild(typeNode)
          tableHead.appendChild(dateNode)

          let tableBody = document.createElement('div');
          tableBody.style.marginTop = "8px"
          tableBody.classList.add('tableBody')
          tableBody.style.width = "100%"
          container.appendChild(tableBody)

          let table = document.createElement('table')
          table.style.width = "100%"
          table.style.borderCollapse = "collapse"
          appendTr(table,inboundDetail)
          tableBody.appendChild(table)
          
          newWindow.document.body.appendChild(container);
      }

      return{
          tableData,
          stateOptions,
          ruleForm,
          supplierList,
          loading,
          deleteIb,
          getIb,
          getSupplierData,
          convertStatus,
          getStatus,
          getTag,
          resetForm,
          searchInbound,
          createIb,
          inbound,
          printInbound
      }
  },
    
}
</script>

<style scoped>
</style>