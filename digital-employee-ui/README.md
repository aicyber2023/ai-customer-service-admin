# 1.下载依赖
- 要求Node 14.0+
- npm install

#   2.启动项目
- npm run dev

#   打包项目
-  npm run build:prod

# 配置说明
- public/config.js 配置的是全局变量用来指定服务器路径

# 打包文件说明
- customer-service-server 配置的是全局变量用来指定请求路径

  _1._ baseUrl  用于指定接口请求的根路径(后端服务地址)
  - baseUrl 指的是后端服务的地址 加入/prod-api使用nginx反向代理访问
  - 注意：如果后端服务没有使用反向代理，则 baseURL 应该配置为后端服务的真实地址

  _2._ chatUrl 用于指定访问customer-service-chat 前端页面的访问路径
  - chatUrl指的是 在管理系统后台进入的智能客服对话页面访问的地址
  - 注意: 如果想在浏览器中直接访问对话页面 需要指定身份 在url后边加入 参数即可 例如http://127.0.0.1:28001/#/?isVisitor=1&id=78
  - isVisitor=1 代表访问模式为访客模式 id=78 代表 使用第78号客服进行对话
