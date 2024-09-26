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
    <el-form-item label="出库状态" prop="outboundStatus">
      <el-segmented v-model="ruleForm.outboundStatus" :options="stateOptions" />
    </el-form-item>
    <el-form-item label="出库单号" prop="outboundNum">
      <el-input v-model="ruleForm.outboundNum" />
    </el-form-item>
    <el-form-item label="客户" prop="customer">
      <el-select v-model="ruleForm.customer" placeholder="客户">
        <el-option label='全部' value='' />
        <!-- <el-option label="1WT" value="1WT" />
        <el-option label="2GC" value="2GC" />
        <el-option label="3JG" value="3JG" />
        <el-option label="4WY" value="4WY" /> -->
        <el-option
          v-for="item in customerList"
          :key="item.id"
          :label="item.customerCode"
          :value="item.customerCode"
        />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent = "searchOutbound">
        查询
      </el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>

  <!-- 创建出库单入口 -->

  <el-button type="primary" color="#6495ED" size="large" @click="createOb">+创建出库单</el-button>

    <!-- 列表 -->
    <el-table :data="tableData" v-loading="loading" row-key="id"    height="500" style="width: 100%"  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="outboundNum" label="出库单号" width="300" />
        <el-table-column prop="customer" label="客户" width="250" />
        <el-table-column prop="outboundStatus" label="出库状态" width="250" >
          <template v-slot:default="scope">
            <el-tag :type=getTag(scope.row.outboundStatus)>{{ getStatus(scope.row.outboundStatus) }}</el-tag>            
          </template>
        </el-table-column>
        <el-table-column  label="备注" />
        <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
                <el-button
                v-if="getVisible(scope.row.outboundStatus)"
                link
                type="primary"
                size="small"
                @click.prevent="outbound(scope.row.outboundNum)"
                >
                出库
                </el-button>
                <el-button
                link
                type="primary"
                size="small"
                @click.prevent="printOutbound(scope.$index)"
                >
                打印出库单
                </el-button>
                <el-button
                v-if="getVisible(scope.row.outboundStatus)"
                link
                type="primary"
                size="small"
                @click.prevent="deleteOb(scope.row.id)"
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
    name: "ExWarehouse",
    inject: ['reload'], 
    props:['curMenu'],
    setup() {
        const stateOptions = ['全部', '未出库', '部分出库','已出库']
        const ruleForm = ref({
        outboundStatus : '全部',
        outboundNum: null,
        customer: null
        })
       
        const loading=ref(false)
        const router = useRouter();
        const tableData = ref([])
        const customerList = ref([])
        onMounted(async () => {
          //获取出库单数据
          try {
            const response = await axios.get('http://localhost:8080/outbound'); // 发起 Axios GET 请求
            tableData.value = response.data; // 将响应数据赋给 tableData
          } catch (error) {
            console.error('Error fetching data:', error);
          }
          //获取用户数据
          try {
            const response = await axios.get('http://localhost:8080/customer');
            customerList.value = response.data; 
          } catch (error) {
            console.error('Failed to fetch customer data', error);
          }
       });

      //  测试用数组
      // const tableData=ref([
      //   {
      //     id:'1',
      //     outboundStatus:0,
      //     outboundNum:'12348-66544',
      //     customer:'A'
      //   },
      //   {
      //     id:'2',
      //     outboundStatus:1,
      //     outboundNum:'12348-664234',
      //     customer:'B'
      //   },
      //   {
      //     id:'3',
      //     outboundStatus:2,
      //     outboundNum:'12171-66544',
      //     customer:'C'
      //   }

      // ])
      // const customerList = ref([
      //     {
      //       id:'1',
      //       customerCode:'1WT',
      //       customerName:'wgc'
      //     },
      //     {
      //       id:'2',
      //       customerCode:'2WT',
      //       customerName:'asdc'
      //     },          {
      //       id:'3',
      //       customerCode:'1WT',
      //       customerName:'wrwc'
      //     }
      //   ])

       function convertStatus(state){
        switch(state){
          case '全部':
            return null;
          case '已出库':
            return 0;
          case '部分出库':
            return 1;
          case '未出库':
            return 2;
          default:
            return '';
        }
       }

       function getStatus(state){
        console.log(state)
        switch(state){
          case 0:
            return '已出库';
          case 1:
            return '部分出库';
          case 2:
            return '未出库';
          default:
            return '未出库';
        }
       }

       function getVisible(status){
        if(status == 0){
          return false;
        }
        else{
          return true;
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
          outboundStatus: '全部', // 出库状态字段
          outboundNum: null,    // 出库单号字段
          customer: null,      // 客户字段
        };
      }

      const getCustomerData = async() =>{
          try {
            const response = await axios.get('http://localhost:8080/customer');
            customerList.value = response.data; // 假设返回的数据格式是 [{ label: '1WT', value: '1WT' }, ...]
          } catch (error) {
            console.error('Failed to fetch customer data', error);
          }
        }

        const getOb = async () => {
          try {
            console.log('调用了getIb');
            loading.value = true;
            const response = await axios.get('http://localhost:8080/outbound'); // 发起 Axios GET 请求
            tableData.value = response.data; // 将响应数据赋给 tableData
            loading.value = false
          } catch (error) {
            console.error('Error fetching data:', error);
          }
        }

        const deleteOb = async (id) =>{
          console.log(id);
          ElMessageBox.confirm(
            '确定删除吗？', 
            '提示', 
            { 
              confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' 
            }).then(async () => { 
              // 用户点击确定后的操作，比如调用删除接口等 
                  await axios.delete('http://localhost:8080/outbound/' + id);
                  console.log(`Deleted outbound with id: ${id}`);
                try {
                  console.log('调用了getOb');
                  loading.value = true;
                  const response = await axios.get('http://localhost:8080/outbound'); // 发起 Axios GET 请求
                  tableData.value = [...response.data]; // 将响应数据赋给 tableData
                  loading.value = false
                } catch (error) {
                  console.error('Error fetching data:', error);
                }
            }).catch((error) => { 
                console.error('用户取消了操作:', error);
                });
        }

        const searchOutbound = async () => {
          try {
            const response = await axios.get('http://localhost:8080/outbound', {
              params: {
                outboundStatus: convertStatus(ruleForm.value.outboundStatus),
                outboundNum: ruleForm.value.outboundNum,
                customer: ruleForm.value.customer
              }
            });
            tableData.value = [...response.data]; // 更新 tableData 的引用
          } catch (error) {
            console.error('Error fetching data:', error);
          }
        };

        const createOb = () =>{
          router.push("/createOutbound");
        }     
        

        // const inbound = (inboundNum)=>{
        //   router.push("/inbound?inboundNum="+inboundNum)
        // }
        
        const outbound = (outboundNum)=>{
            router.push({
            path: `/outbound/${outboundNum}`
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

      const printOutbound = async function(index) {
          let customer = customerList.value[index];
          let outboundNum = tableData.value[index].outboundNum
          let currentDate = new Date()
          let outboundDetail = await axios.get(`http://localhost:8080/outboundDetail/${outboundNum}`);
          console.log('准备创建新界面')
          console.log('获取到的数据为',outboundDetail)

          let newWindow = window.open('',`出库单--${customer.customerName}`,null)
          newWindow.document.body.innerHTML = ''

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
          title.innerHTML = `出库单--${customer.customerName}`
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
          let customerNode = document.createElement('div')
          let outboundNumNode = document.createElement('div')
          let typeNode = document.createElement('div')
          let dateNode = document.createElement('div')

          customerNode.classList.add('left-top','innerTitle')
          customerNode.innerHTML = `供应商: ${customer.customerName}`
          customerNode.style.fontSize = "16px"
          customerNode.style.padding = "8px"
          customerNode.style.position = "absolute"
          customerNode.style.left = "0"
          customerNode.style.top = "0"

          typeNode.classList.add('left-down','innerTitle')
          typeNode.innerHTML = '出库方式: 采购出库'
          typeNode.style.fontSize = "16px"
          typeNode.style.padding = "8px"
          typeNode.style.position = "absolute"
          typeNode.style.left = "0"
          typeNode.style.top = "50%"

          outboundNumNode.classList.add('right-top','innerTitle')
          outboundNumNode.innerHTML = `出库单编号: ${outboundNum}`
          outboundNumNode.style.fontSize = "16px"
          outboundNumNode.style.padding = "8px"
          outboundNumNode.style.position = "absolute"
          outboundNumNode.style.left = "50%"
          outboundNumNode.style.top = "0"

          dateNode.classList.add('right-down','innerTitle')
          dateNode.innerHTML = `时间: ${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}`+' '+`${currentDate.getHours()}`+':'+`${currentDate.getMinutes()}`+':'+`${currentDate.getSeconds()}`
          dateNode.style.fontSize = "16px"
          dateNode.style.padding = "8px"
          dateNode.style.position = "absolute"
          dateNode.style.left = "50%"
          dateNode.style.top = "50%"

          container.appendChild(tableHead)
          tableHead.appendChild(customerNode)
          tableHead.appendChild(outboundNumNode)
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
          appendTr(table,outboundDetail)
          tableBody.appendChild(table)
          
          newWindow.document.body.appendChild(container);
      }

        return{
            tableData,
            stateOptions,
            ruleForm,
            customerList,
            loading,
            deleteOb,
            getOb,
            printOutbound,
            getCustomerData,
            convertStatus,
            getStatus,
            getVisible,
            getTag,
            resetForm,
            searchOutbound,
            createOb,
            outbound

        }
    },
      
  }
</script>

<style scoped>
</style>
