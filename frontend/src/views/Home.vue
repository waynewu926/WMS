<template>
  <div>
    <div class="nav">
      <router-link to="/dashboard" class="title-link">
        <div class="title">WMS仓库管理系统</div>
      </router-link>
      <el-menu
        :collapse="false"
        background-color="#2b293d"
        text-color="#fff"
        active-text-color="#ffd04b"
        @click="changeMenu"
        router
        :default-active="data.curMenu"
      >
        <el-sub-menu index="1" class="submenu">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>入库/出库</span>
          </template>
          <el-menu-item class="single-item" index="/warehouse"
            ><template #title>入库</template></el-menu-item
          >
          <el-menu-item class="single-item" index="/exWarehouse"
            ><template #title>出库</template></el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="2" class="submenu">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>零件</span>
          </template>
          <el-menu-item class="single-item" index="/itemInfo"
            ><template #title>零件</template></el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="3" class="submenu">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>供应商/客户</span>
          </template>
          <el-menu-item class="single-item" index="/supplierInfo"
            ><template #title>供应商</template></el-menu-item
          >
          <el-menu-item class="single-item" index="/customerInfo"
            ><template #title>客户</template></el-menu-item
          >
        </el-sub-menu>
        <el-sub-menu index="4" class="submenu">
          <template #title>
            <el-icon><icon-menu /></el-icon>
            <span>库存</span>
          </template>
          <el-menu-item class="single-item" index="/inventoryInfo"
            ><template #title>库存</template></el-menu-item
          >
        </el-sub-menu>
      </el-menu>
    </div>

    <div class="header"></div>

    <div class="nav-icon-container">
      <div
        v-for="item in data.navIconList"
        :key="item.title"
        :class="{
          'single-nav-icon': true,
          'single-active-nav-icon': item.path == data.curMenu,
        }"
      >
        <span class="single-title" @click="changeNav(item)">{{
          item.title
        }}</span>
        <el-icon class="single-close" @click="closeNav(item)"
          ><Close
        /></el-icon>
      </div>
    </div>

    <el-card class="content">
      <router-view :close-nav="closeNav"></router-view>
    </el-card>
  </div>
</template>

<script>
import { onBeforeMount, reactive } from "vue";
import { useRouter } from "vue-router";
import { match } from "path-to-regexp";
export default {
  name: "Home",
  setup() {
    const myRouter = useRouter();
    const data = reactive({
      curMenu: "",
      navIconList: [],
      routerList: [
        {
          title: "入库界面",
          path: "/warehouse",
        },
        {
          title: "创建入库单",
          path: "/createInbound",
        },
        {
          title: "入库",
          path: "/inbound/:inboundNum",
        },
        {
          title: "出库界面",
          path: "/exWarehouse",
        },
        {
          title: "创建出库单",
          path: "/createOutbound",
        },
        {
          title: "出库",
          path: "/outbound/:outboundNum",
        },
        {
          title: "供应商详情",
          path: "/supplierInfo",
        },
        {
          title: "零件详情",
          path: "/itemInfo",
        },
        {
          title: "客户详情",
          path: "/customerInfo",
        },
        {
          title: "库存详情",
          path: "/inventoryInfo",
        },
      ],
    });
    onBeforeMount(() => {
      data.curMenu = myRouter.currentRoute.value.path;
      changeMenu();
      // 设置路由监听器
      myRouter.afterEach((to, from) => {
        data.curMenu = to.path;
        changeMenu();
      });
    });

    function changeMenu() {
      data.curMenu = myRouter.currentRoute.value.path;
      let findFlag = false;

      data.navIconList.forEach((item) => {
        if (item.path === data.curMenu) {
          findFlag = true;
          return;
        }
      });

      if (!findFlag) {
        for (let item of data.routerList) {
          const matcher = match(item.path);
          const matchResult = matcher(data.curMenu);

          if (matchResult) {
            // 生成实际路径
            const actualPath = item.path.replace(
              /:([^/]+)/g,
              (_, key) => matchResult.params[key] || ""
            );
            const newNav = {
              title: item.title,
              path: actualPath,
            };
            data.navIconList.push(newNav);
            break;
          }
        }
      }
    }

    function changeNav(item) {
      data.curMenu = item.path;
      myRouter.push({
        path: data.curMenu,
      });
    }

    function closeNav(item) {
      var index = 0;
      data.navIconList.forEach((navItem) => {
        if (navItem.path == item.path) {
          return;
        }
        index++;
      });
      const deletePath = data.navIconList[index].path;
      data.navIconList = data.navIconList.filter(
        (navItem) => navItem.path != item.path
      );

      if (data.navIconList.length == 0) {
        data.navIconList.push({
          title: "欢迎",
          path: "/dashboard",
        });
        data.curMenu = "/dashboard";
        myRouter.push({
          path: "/dashboard",
        });
      } else {
        if (data.curMenu == deletePath) {
          data.curMenu = data.navIconList[index - 1].path;
          myRouter.push({
            path: data.navIconList[index - 1].path,
          });
        }
      }
    }

    return {
      data,
      changeMenu,
      changeNav,
      closeNav,
    };
  },
};
</script>

<style scoped>
.title-link {
  text-decoration: none;
  color: inherit;
}

.title {
  font-size: 2.5em;
  font-weight: bold;
  margin-bottom: 20px;
  color: white;
  cursor: pointer;
  transition: color 0.3s ease, transform 0.3s ease;
}

.nav {
  position: absolute;
  left: 0;
  top: 0;
  width: 200px;
  height: 100vh;
  background-color: #100632;
  color: white;
}
.title {
  text-align: center;
  height: 60px;
  line-height: 60px;
  width: 200px;
  border-bottom: 1px solid #666666;
  background-color: #24243f;
  font-size: 20px;
}
.submenu {
  background-color: #343448;
}
.single-item {
  height: 50px;
  width: 200px;
  line-height: 50px;
  background-color: #2b293d;
  border-bottom: 1px solid #2e2e2e;
  padding-left: 60px !important;
}
.single-item:hover {
  background-color: #0b062d;
  cursor: pointer;
}

/* 头部区域样式 */
.header {
  position: absolute;
  left: 200px;
  top: 0;
  width: calc(100vw - 200px);
  background-color: #24243f;
  height: 60px;
}

/* 页签区域样式 */
.nav-icon-container {
  position: absolute;
  left: 200px;
  top: 60px;
  width: calc(100vw - 200px);
  height: 30px;
  box-sizing: border-box;
  padding: 3px;
  padding-left: 5px;
}
/* 单个导航页签的样式 */
.single-nav-icon {
  background-color: white;
  color: blue;
  width: 100px;
  height: 25px;
  line-height: 25px;
  text-align: center;
  font-size: 14px;
  border-radius: 5px;
  border: 1px #ecedef solid;
  display: inline-block;
  margin-right: 10px;
}
.single-title:hover {
  cursor: pointer;
}
/* 关闭页签的图标样式 */
.single-close {
  margin-left: 5px;
  font-size: 16px;
  position: relative;
  top: 3px;
}
.single-close:hover {
  cursor: pointer;
  color: black;
}
/* 激活的页签的样式 */
.single-active-nav-icon {
  background-color: #150641;
  color: white;
}

/* 内容区域容器样式 */
.content {
  overflow-y: auto;
  position: absolute;
  left: 205px;
  top: 95px;
  width: calc(100vw - 210px);
  height: calc(100vh - 100px);
}
</style>
