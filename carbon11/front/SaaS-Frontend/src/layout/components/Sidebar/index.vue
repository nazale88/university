<template>
  <div :class="{ 'has-logo': showLogo }" class="sidebar-container">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <!-- 移除 {{ permission_routes }} 调试代码，避免影响布局 -->
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="route in sidebarList"
          :key="route.id"
          :item="route"
          :base-path="route.menuUrl"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/styles/variables.scss";
import { getSliderMenus } from "@/api/configApi";

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      sidebarList: [],
    };
  },
  created() {
    this.loadSlidata();
  },
  methods: {
    loadSlidata() {
      getSliderMenus()
        .then((res) => {
          this.sidebarList = res.data || res;
          console.log("侧边菜单数据:", res);
        })
        .catch((err) => {
          console.error('获取侧边菜单失败:', err);
          this.sidebarList = [];
        });
    }
  },
  computed: {
    ...mapGetters(["permission_routes", "sidebar"]),
    activeMenu() {
      const { meta, path } = this.$route;
      return meta.activeMenu || path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    },
  },
};
</script>

<!-- 核心：加 scoped + ::v-deep 覆盖全局样式 -->
<style lang="scss" scoped>
// 1. 覆盖侧边栏容器样式：半透明背景 + 模糊效果
::v-deep .sidebar-container {
  background-color: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(10px) !important;
  -webkit-backdrop-filter: blur(10px) !important; /* 兼容 Safari */
  height: 100vh !important; /* 确保侧边栏全屏 */
  border-right: none !important; /* 可选：清除右侧边框，更美观 */
}

// 2. 覆盖 Element UI 菜单样式：清除白色背景，继承容器半透明
::v-deep .el-menu {
  background-color: transparent !important; /* 关键：清除全局的 white 背景 */
  border-right: none !important; /* 清除菜单默认右侧边框 */
}

// 3. 覆盖菜单项 hover/选中样式（可选，避免白色背景突兀）
::v-deep .el-menu-item:hover,
::v-deep .el-submenu-item:hover,
::v-deep .el-menu-item.is-active,
::v-deep .el-submenu-item.is-active {
  background-color: rgba(243, 252, 249, 0.8) !important; /* 半透明hover色，匹配全局 $menuHover */
}

// 4. 覆盖滚动条容器样式：清除默认背景
::v-deep .scrollbar-wrapper {
  background-color: transparent !important;
}

// 5. 覆盖 Logo 容器样式（若 Logo 区域有白色背景）
::v-deep .sidebar-container .has-logo {
  background-color: transparent !important;
}
</style>
