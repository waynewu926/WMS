import { createRouter, createWebHashHistory } from "vue-router";

import Home from "@/views/Home.vue";
import Warehouse from "@/views/Warehouse.vue";
import CreateInbound from "@/views/CreateInbound.vue";
import UserManage from "@/views/UserManage.vue";
import Inbound from "@/views/Inbound.vue";
import ExWarehouse from "@/views/ExWarehouse.vue";
import CreateOutbound from "@/views/CreateOutbound.vue";
import Outbound from "@/views/Outbound.vue";
import ItemInfo from "@/views/ItemInfo.vue";
import SupplierInfo from "@/views/SupplierInfo.vue";
import CustomerInfo from "@/views/CustomerInfo.vue";
import InventoryInfo from "@/views/InventoryInfo.vue";
import MainDashboard from "@/views/MainDashboard.vue";
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
    redirect: "/warehouse",
    children: [
      {
        path: "/warehouse",
        name: "Warehouse",
        component: Warehouse,
      },
      {
        path: "/createInbound",
        name: "CreateInbound",
        component: CreateInbound,
      },
      {
        path: "/inbound/:inboundNum",
        name: "Inbound",
        component: Inbound,
      },
      {
        path: "/userManage",
        name: "UserManage",
        component: UserManage,
      },
      {
        path: "/exWarehouse",
        name: "ExWarehouse",
        component: ExWarehouse,
      },
      {
        path: "/createOutbound",
        name: "CreateOutbound",
        component: CreateOutbound,
      },
      {
        path: "/outbound/:outboundNum",
        name: "Outbound",
        component: Outbound,
      },
      {
        path: "/itemInfo",
        name: "ItemInfo",
        component: ItemInfo,
      },
      {
        path: "/supplierInfo",
        name: "SupplierInfo",
        component: SupplierInfo,
      },
      {
        path: "/customerInfo",
        name: "CustomerInfo",
        component: CustomerInfo,
      },
      {
        path: "/inventoryInfo",
        name: "InventoryInfo",
        component: InventoryInfo,
      },
      {
        path: "/dashboard",
        name: "Dashboard",
        component: MainDashboard,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
