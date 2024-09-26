<template>
    <div class="inboundUpdate_background">
        <div class="inboundUpdate_container">
            <div class="outbound-head">
                <div class="left">出库单号： {{ outboundNum }}</div>
            </div>
            <div class="inboundUpdate_header">
                <span class="inboundUpdateSpan">零件明细</span>
                <el-button type="primary" plain @click="batchModification">批量设置出库状态</el-button>
            </div>
            <div></div>
            <div class="inboundUpdate_SubContainer">
                <div class="inboundUpdate_body">
                <el-table
                    :data="tableData"
                    @selection-change="selectionChange"
                >
                    <el-table-column type="selection" width="36"></el-table-column>
                    <el-table-column label="零件名称" prop="itemNum" width="128"/>
                    <el-table-column label="零件编号" prop="id" width="96"/>
                    <el-table-column label="计划数量" prop="planQuantity" width="96"/>
                    <el-table-column label="实际数量" width="144">
                        <template #default="scope">
                            <el-input-number
                                v-model="scope.row.realQuantity"
                                :min="minQuantity[scope.$index]"
                                :max="scope.row.planQuantity"
                                size="small"
                            />
                        </template>
                    </el-table-column>
                    <el-table-column label="出库状态" width="144">
                        <template #default="scope">
                            <el-select
                                :modelValue="scope.row.itemStatusName"
                                @update:modelValue="changeStatus(scope.row, $event)"
                                style="width: 120px"
                            >
                                <el-option
                                    v-for="status in outboundStatus.statusArr"
                                    :key="status.value"
                                    :label="status.key"
                                    :value="status.value"
                                >
                                </el-option>
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="备注" width="200">
                        <template #default="scope">
                            <el-input
                                v-model="scope.row.remark"
                                style="width: 160px"
                                maxlength="30"
                                minlength="0"
                                placeholder="请输入备注"
                                show-word-limit
                                small
                                type="text"
                            />
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="inboundUpdate_footer">
                <el-button @click="handleNavClose">取消</el-button>
                <el-button type="primary" @click="putInboundDetail">保存</el-button>
            </div>
            </div>
        </div>
    </div>
  </template>
  
  <script>
  import { useRouter } from 'vue-router';
  import { ref,reactive, onMounted , onBeforeMount} from 'vue';
  import { h } from 'vue'
  import BoxElSelect from './boxElSelect.vue'
  import outboundStatus from '../router/outboundStatus.js';
  import axios from 'axios'
  import { ElMessageBox } from 'element-plus';
  
  export default {
    name: "Outbound",
      props: {
          closeNav: {
              type: Function,
              required: true
          }
      },
      setup(props) {
          const { closeNav } = props;
          const router = useRouter();
          const outboundNum = ref(0);
          const choosenStatus = ref('');
          const currentStatus = ref(0);
          const choosenRows = ref([]);
          const minQuantity = ref([16]);
          const outboundStatusReactive = reactive(outboundStatus);
          const tableData = reactive([
              {
                  itemNum: '111',
                  planQuantity: '22222',
                  realQuantity: 17,
                  itemStatus: '0',
                  remark: '',
                  index: 0,
                  itemStatusName: '已出库'
              }
          ]);
  
          const putInboundDetail = () => {
            console.log('要发送请求了')
              axios.put('http://localhost:8080/outboundDetail', {
                  outboundNum: outboundNum.value,
                  outboundDetail: tableData.map(el => { return {
                      itemNum: el.itemNum,
                      realQuantity: el.realQuantity,
                      itemStatus: el.itemStatus
                  } })
              }).then(()=>{
                console.log('成功发送')
              }).catch(()=>{
                console.log('发送失败')
              });
            
            handleNavClose();
          };
  
          const changeStatus = (obj,val) => {
              obj.itemStatus = val;
              obj.itemStatusName = outboundStatus.statusArr[val].key
              console.log(obj,val)
          }
  
          const updateStatus = (val) => {
              currentStatus.value = val;
          };
  
          const selectionChange = (selection) => {
              choosenRows.value = selection.map(el => el.index);
              console.log(choosenRows.value);
          };
  
          const handleNavClose = function() {
              let itemToClose = { title:'出库', path:`/outbound/${outboundNum.value}` }
              closeNav(itemToClose);
          }
  
          const batchModification = () => {
              ElMessageBox({
                  title: '批量设置出库状态',
                  message: h(
                      'p',
                      {
                          style: {
                              fontSize: '16px'
                          }
                      },
                      [
                          '将勾选条目的出库状态修改为: ',
                          h(
                              BoxElSelect,
                              {
                                  modelValue: currentStatus.value,
                                  'onUpdate:modelValue': updateStatus,
                                  statusArr: outboundStatus.statusArr
                              }
                          )
                      ]
                  )
              }).then(() => {
                  console.log(choosenRows.value);
                  console.log(currentStatus.value);
                  choosenRows.value.forEach((el) => {
                      changeStatus(tableData[el],currentStatus.value)
                  });
              }).catch((reason) => {
                  return reason
              });
          };
  
          onBeforeMount(()=>{
            // 设置路由监听器
            router.afterEach((to, from) => {
                let num = router.currentRoute.value.params.outboundNum;
                if (num) {
                  outboundNum.value = num;
                }
            });
          })

          onMounted(() => {
                const num = router.currentRoute.value.params.outboundNum;
                if (num) {
                  outboundNum.value = num;
                }
              console.log(currentStatus.value);
              axios.get(`http://localhost:8080/outboundDetail/${outboundNum.value}`)
                  .then(res => {
                      let result = res.data;
                      tableData.splice(0);
                      minQuantity.value = [];
                      result.forEach((el, index) => {
                          if (!tableData[index]) {
                              tableData[index] = {};
                          }
  
                          Object.assign(tableData[index], el);
                          tableData[index]['index'] = index;
  
                          tableData[index].itemStatusName = outboundStatus.statusArr[tableData[index].itemStatus].key;
  
                          tableData[index].planQuantity = +tableData[index].planQuantity
                          tableData[index].realQuantity = +tableData[index].realQuantity
  
                          minQuantity.value[index] = el['realQuantity'];
                      });
                      return result;
                  })
                  .catch(err => {
                      console.error(err);
                  });
          });
  
          return {
              router,
              outboundNum,
              choosenStatus,
              outboundStatus: outboundStatusReactive,
              currentStatus,
              choosenRows,
              minQuantity,
              tableData,
              handleNavClose,
              changeStatus,
              putInboundDetail,
              updateStatus,
              selectionChange,
              batchModification
          };
      }
  };
  </script>
  
  <style>
  .outbound-head {
    position: relative;
    width: 100%;
    height: 32px;
    margin:0 auto;
    padding-bottom: 24px;
    margin-bottom: 24px;
    border-bottom: 1px solid #aaa
  }

  .left {
    position: absolute;
    width: fit-content;
    font-weight: bold;
    font-size: 16px;
    left: 0px;
    top: 0px;
  }
  .inboundUpdate_background {
      width: 100%;
      margin: 0;
      padding: 0;
  }
  .inboundUpdateSpan {
      width: 100px;
      font-weight: bold;
      font-size: 24px;
      margin-right: 24px;
  }
  
  .inboundUpdate_container {
      margin:auto;
      width:fit-content;
  }
  
  
  .inboundUpdate_SubContainer {
      display: flex;
      width: 100%;
      margin: 24px auto;
      justify-content: center;
      align-items: center;
      flex-direction: column;
  }
  
  .inboundUpdate_body {
      width: 100%
  }
  
  .inboundUpdate_footer {
      margin: 24px auto;
  }
  </style>