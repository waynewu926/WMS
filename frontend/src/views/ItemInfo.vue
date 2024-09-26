<template>
  <div class="item-info-container">
    <h1 class="page-title">零件信息</h1>
    <div class="item-info">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form ref="searchForm" :model="searchForm" label-width="100px" class="search-form">
          <el-form-item label="零件号">
            <el-input v-model="searchForm.itemNum" placeholder="搜索零件号"></el-input>
          </el-form-item>
          <el-form-item label="零件编号">
            <el-input v-model="searchForm.itemSerial" placeholder="搜索零件编号"></el-input>
          </el-form-item>
          <el-form-item label="供应商">
            <el-select v-model="searchForm.supplier" placeholder="选择或输入供应商" filterable clearable>
              <el-option label="全部" value=""></el-option>
              <el-option
                v-for="supplier in suppliers"
                :key="supplier.id"
                :label="supplier.supplierCode"
                :value="supplier.supplierCode"
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
          <el-table-column prop="itemNum" label="零件号" width="300"></el-table-column>
          <el-table-column prop="itemSerial" label="零件编号" width="300"></el-table-column>
          <el-table-column prop="supplier" label="供应商" width="300"></el-table-column>
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
      title="零件详情"
      :draggable="true"
      width="50%"
    >
      <table v-if="selectedItem">
        <tr>
          <td>零件编号:</td>
          <td>{{ selectedItem.itemSerial }}</td>
          <td>供应商:</td>
          <td>{{ selectedItem.supplier }}</td>
        </tr>
        <tr>
          <td>创建人:</td>
          <td>{{ selectedItem.createdBy }}</td>
          <td>创建时间:</td>
          <td>{{ selectedItem.createdTime }}</td>
        </tr>
        <tr>
          <td>更新人:</td>
          <td>{{ selectedItem.updatedBy }}</td>
          <td>更新时间:</td>
          <td>{{ selectedItem.updatedTime }}</td>
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
        itemNum: "",
        supplier: "",
        itemSerial: ""
      },
      searchResults: [],
      suppliers: [],
      selectedItem: null,
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
        console.log("开始搜索，查询参数：", this.searchForm.itemNum, this.searchForm.supplier, this.searchForm.itemSerial);
        const response = await axios.get('http://localhost:8080/item', {
          params: {
            itemNum: this.searchForm.itemNum || null,
            supplier: this.searchForm.supplier || null,
            itemSerial: this.searchForm.itemSerial || null
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
      this.searchForm.supplier = "";
      this.searchForm.itemSerial = "";
      this.search();
    },
    viewDetails(item) {
      this.selectedItem = item;
      this.dialogVisible = true;
      console.log("查看详情：", this.selectedItem, this.dialogVisible);
    },
    closeDetails() {
      this.dialogVisible = false;
      this.selectedItem = null;
      console.log("关闭详情：", this.selectedItem, this.dialogVisible);
    },
    goToPage(page) {
      this.currentPage = page;
    },
    async loadSuppliers() {
      try {
        const response = await axios.get('http://localhost:8080/supplier');
        if (response.data) {
          this.suppliers = response.data.map(supplier => ({
            id: supplier.id,
            supplierCode: supplier.supplierCode,
            supplierName: supplier.supplierName
          }));
        }
      } catch (error) {
        console.error("加载供应商失败", error);
      }
    }
  },
  mounted() {
    this.loadSuppliers();
    this.search(); // 初始化加载所有零件数据
  }
};
</script>

<style scoped>
.item-info-container {
  display: flex;
  flex-direction: column;
  height: 85vh;
  align-items: center;
  justify-content: center;
}
.item-info {
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
