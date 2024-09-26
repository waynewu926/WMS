<template>
    <div class="inventory-info-container">
      <h1 class="page-title">库存信息</h1>
      <div class="inventory-info">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <el-form ref="searchForm" :model="searchForm" label-width="100px" class="search-form">
            <el-form-item label="零件号">
              <el-input v-model="searchForm.itemNum" placeholder="搜索零件号"></el-input>
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
            <el-table-column prop="itemNum" label="零件号" width="420"></el-table-column>
            <el-table-column prop="quantity" label="库存量" width="420"></el-table-column>
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
        title="库存详情"
        :draggable="true"
        width="50%"
      >
        <table v-if="selectedInventory">
          <tr>
            <td>零件号:</td>
            <td>{{ selectedInventory.itemNum }}</td>
            <td>库存量:</td>
            <td>{{ selectedInventory.quantity }}</td>
          </tr>
          <tr>
            <td>创建人:</td>
            <td>{{ selectedInventory.createdBy }}</td>
            <td>创建时间:</td>
            <td>{{ selectedInventory.createdTime }}</td>
          </tr>
          <tr>
            <td>更新人:</td>
            <td>{{ selectedInventory.updatedBy }}</td>
            <td>更新时间:</td>
            <td>{{ selectedInventory.updatedTime }}</td>
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
          itemNum: ""
        },
        searchResults: [],
        selectedInventory: null,
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
          console.log("开始搜索，查询参数：", this.searchForm.itemNum);
          const response = await axios.get('http://localhost:8080/inventory', {
            params: {
              itemNum: this.searchForm.itemNum || null
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
        this.searchForm.itemNum = "";
        this.search();
      },
      viewDetails(inventory) {
        this.selectedInventory = inventory;
        this.dialogVisible = true;
        console.log("查看详情：", this.selectedInventory, this.dialogVisible);
      },
      closeDetails() {
        this.dialogVisible = false;
        this.selectedInventory = null;
        console.log("关闭详情：", this.selectedInventory, this.dialogVisible);
      },
      goToPage(page) {
        this.currentPage = page;
      }
    },
    mounted() {
      this.search(); // 初始化加载所有库存数据
    }
  };
  </script>
  
  <style scoped>
  .inventory-info-container {
    display: flex;
    flex-direction: column;
    height: 85vh;
    align-items: center;
    justify-content: center;
  }
  .inventory-info {
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
  