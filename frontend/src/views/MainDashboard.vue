<template>
  <div class="dashboard-container">
    <h1 class="page-title">WMS仓库管理系统</h1>
    <div class="dashboard-grid">
      <div class="dashboard-item" @click="navigateToHome('/warehouse')">
        <el-icon><i class="el-icon-box"></i></el-icon>
        <span>入库/出库</span>
      </div>
      <div class="dashboard-item" @click="navigateToHome('/itemInfo')">
        <el-icon><i class="el-icon-s-tools"></i></el-icon>
        <span>零件</span>
      </div>
      <div class="dashboard-item" @click="navigateToHome('/supplierInfo')">
        <el-icon><i class="el-icon-s-custom"></i></el-icon>
        <span>供应商/客户</span>
      </div>
      <div class="dashboard-item" @click="navigateToHome('/inventoryInfo')">
        <el-icon><i class="el-icon-s-data"></i></el-icon>
        <span>库存</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    navigateToHome(path) {
      this.$router.push(path);
    }
  }
};
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 85vh;
  background-color: #f5f5f5;
}

.page-title {
  font-size: 2.5em;
  font-weight: bold;
  margin-bottom: 50px;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px;
  width: 80%;
}

.dashboard-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 200px;
  background-color: #778899;
  color: white;
  font-size: 1.5em;
  font-weight: bold;
  border-radius: 15px;
  cursor: pointer;
  transition: transform 0.3s, background-color 0.3s;
}

.dashboard-item:hover {
  transform: scale(1.05);
  background-color: #B0C4DE;
}

.dashboard-item el-icon {
  font-size: 3em;
  margin-bottom: 10px;
}
</style>
