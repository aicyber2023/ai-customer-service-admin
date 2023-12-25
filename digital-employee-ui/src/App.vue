<template>
  <div id="app">
    <router-view/>
    <theme-picker/>
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";

export default {
  name: "App",
  components: {ThemePicker},
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  },
  mounted() {
    this.$store.dispatch('settings/changeSetting', {
      key: 'theme',
      value: "#1d93ab"
    })
  },
};
</script>
<style>
.el-dialog {
  border-radius: 30px !important;
}

.el-tooltip__popper {
  background: #e7f8ff !important;
  color: #1d93ab !important;

  .popper__arrow::after {
    border-bottom-color: #1d93ab !important;
    border-top-color: #1d93ab !important;
  }
}

#app .theme-picker {
  display: none;
}
</style>
