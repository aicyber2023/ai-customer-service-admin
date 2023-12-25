<template>
  <div>
    <iframe
      :src="getSrc()"
      id="ifr1"
      name="ifr1"
      scrolling="yes"
      class="iframe"
      allow="clipboard-read; clipboard-write"
    >
      <p>Your browser does not support iframes.</p>
    </iframe>
  </div>
</template>
<script>

export default {
  data() {
    return {
      id: "",
      templateId: "",
      token: "",
      createBy: "",
    };
  },
  created() {
    this.id = this.$route.query.id;
    this.templateId = this.$route.query.templateId;
    this.token = this.$route.query.header;
    this.createBy = this.$route.query.createBy;
    console.log(this.$route.query.id, this.$route.query.templateId, this.$route.query.header, this.$route.query.createBy);
    // var iframe = document.getElementById('ifr1').contentWindow;
    let _this = this;
    window.addEventListener('message', this.handleMessage);
  },
  methods: {
    getSrc() {
      // return `${window.cfg.chatUrl}/#/?isTest=1&id=${this.id}&templateId=${this.templateId}&header=${this.token}&createBy=${this.createBy}`
      // return `${window.cfg.chatUrl}/#/?isTest=1&id=${this.id}&templateId=${this.templateId}&header=${this.token}&createBy=${this.createBy}`
      return `${window.cfg.chatUrl}/#/?isTest=1&id=${this.id}&templateId=${this.templateId}&header=${this.token}&createBy=${this.createBy}`
    },
    handleMessage(event) {
      // 处理接收到的消息
      if (event.data == "close") {
        this.$tab.closePage(this.$route).then(({visitedViews}) => {
          if (this.isActive(this.$route)) {
            console.log(visitedViews)
            this.toLastView(visitedViews, this.$route)
          }
        })
      }
    },
    isActive(route) {
      return route.path === this.$route.path
    },
    toLastView(visitedViews, view) {
      const latestView = visitedViews.slice(-1)[0]
      if (latestView) {
        this.$router.push(latestView.fullPath)
      } else {
        // now the default is to redirect to the home page if there is no tags-view,
        // you can adjust it according to your needs.
        if (view.name === 'Dashboard') {
          // to reload home page
          this.$router.replace({path: '/redirect' + view.fullPath})
        } else {
          this.$router.push('/')
        }
      }
    },
  },
  beforeDestroy() {
    // 在组件销毁时移除事件监听器，以防止内存泄漏
    window.removeEventListener('message', this.handleMessage);
  }
};
</script>
<style lang="scss" scope>
.iframe {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background: #fff;
  overflow-y: hidden;
  border: none;
}
</style>
