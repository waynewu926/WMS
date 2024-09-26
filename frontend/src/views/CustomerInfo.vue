<template>
    <div class="customer-info-container">
      <h1 class="page-title">客户信息</h1>
      <div class="customer-info">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <el-form ref="searchForm" :model="searchForm" label-width="100px" class="search-form">
            <el-form-item label="客户代码">
              <el-select v-model="searchForm.customerCode" placeholder="选择或输入客户代码" filterable clearable>
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="customer in customers"
                  :key="customer.id"
                  :label="customer.customerCode"
                  :value="customer.customerCode"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="客户姓名">
              <el-select v-model="searchForm.customerName" placeholder="选择或输入客户姓名" filterable clearable>
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="customer in customers"
                  :key="customer.id"
                  :label="customer.customerName"
                  :value="customer.customerName"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="search">搜索</el-button>
              <el-button @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
  
        <!-- 搜索结果列表 -->
        <div class="search-results">
          <el-table :data="paginatedResults" stripe>
            <el-table-column prop="customerCode" label="客户代码" width="420"></el-table-column>
            <el-table-column prop="customerName" label="客户姓名" width="420"></el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
              <template v-slot="scope">
                <el-button @click="viewDetails(scope.row)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
  
        <!-- 分页控制 -->
        <div class="pagination">
          <el-pagination
            layout="prev, pager, next"
            :total="searchResults.length"
            :page-size="itemsPerPage"
            @current-change="goToPage"
          ></el-pagination>
        </div>
      </div>
  
      <!-- 详情弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        title="客户详情"
        :draggable="true"
        width="50%"
      >
        <table v-if="selectedCustomer">
          <tr>
            <td>客户代码:</td>
            <td>{{ selectedCustomer.customerCode }}</td>
            <td>客户名称:</td>
            <td>{{ selectedCustomer.customerName }}</td>
          </tr>
          <tr>
            <td>创建人:</td>
            <td>{{ selectedCustomer.createdBy }}</td>
            <td>创建时间:</td>
            <td>{{ selectedCustomer.createdTime }}</td>
          </tr>
          <tr>
            <td>更新人:</td>
            <td>{{ selectedCustomer.updatedBy }}</td>
            <td>更新时间:</td>
            <td>{{ selectedCustomer.updatedTime }}</td>
          </tr>
        </table>
        <template v-slot:footer>
<span  class="dialog-footer">
          <el-button @click="closeDetails">关闭</el-button>
        </span>
</template>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        searchForm: {
          customerCode: "",
          customerName: ""
        },
        searchResults: [],
        customers: [],
        selectedCustomer: null,
        dialogVisible: false,
        currentPage: 1,
        itemsPerPage: 5,
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.searchResults.length / this.itemsPerPage);
      },
      paginatedResults() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        const end = start + this.itemsPerPage;
        return this.searchResults.slice(start, end);
      },
    },
    methods: {
      async search() {
        try {
          console.log("开始搜索，查询参数：", this.searchForm.customerCode, this.searchForm.customerName);
          const response = await axios.get('http://localhost:8080/customer', {
            params: {
              customerCode: this.searchForm.customerCode || null,
              customerName: this.searchForm.customerName || null
            },
            headers: {
              'Accept': 'application/json'
            }
          });
          console.log("搜索结果：", response.data);
          if (Array.isArray(response.data)) {
            this.searchResults = response.data;
          } else {
            console.error("搜索结果格式不正确", response.data);
          }
          this.currentPage = 1; // 重置到第一页
        } catch (error) {
          console.error("搜索失败", error);
        }
      },
      reset() {
        this.searchForm.customerCode = "";
        this.searchForm.customerName = "";
        this.search();
      },
      viewDetails(customer) {
        this.selectedCustomer = customer;
        this.dialogVisible = true;
        console.log("查看详情：", this.selectedCustomer, this.dialogVisible);
      },
      closeDetails() {
        this.dialogVisible = false;
        this.selectedCustomer = null;
        console.log("关闭详情：", this.selectedCustomer, this.dialogVisible);
      },
      goToPage(page) {
        this.currentPage = page;
      },
      async loadCustomers() {
        try {
          const response = await axios.get('http://localhost:8080/customer');
          if (response.data) {
            this.customers = response.data.map(customer => ({
              id: customer.id,
              customerCode: customer.customerCode,
              customerName: customer.customerName
            }));
          }
        } catch (error) {
          console.error("加载客户失败", error);
        }
      }
    },
    mounted() {
      this.loadCustomers();
      this.search(); // 初始化加载所有客户数据
    }
  };
  </script>
  
  <style scoped>
  .customer-info-container {
    display: flex;
    flex-direction: column;
    height: 85vh;
    align-items: center;
    justify-content: center;
  }
  .customer-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    width: 80%;
  }
  .page-title {
    font-weight: bold;
    text-align: center;
    margin-bottom: 20px;
  }
  .search-bar {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
  }
  .search-form {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
  .el-form-item {
    flex: 1;
    margin-right: 20px;
  }
  .el-form-item:last-child {
    margin-right: 0;
  }
  .search-results {
    flex: 1;
    overflow-y: auto;
    border-top: 1px solid #ccc;
    margin-bottom: 20px;
  }
  .el-table th, .el-table td {
    text-align: center;
    padding: 10px 20px;
  }
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  </style>
  