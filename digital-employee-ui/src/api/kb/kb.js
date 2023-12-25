import request from "@/utils/request";

// 查询列表
export function list(query) {
  return request({
    url: "/de/knowledgeBase/list",
    method: "get",
    params: query,
  });
}

// 查询列表
export function listInviteRecord(query) {
  return request({
    url: "/chat/user/selectInviteRecordList",
    method: "get",
    params: query,
  });
}

// 查询列表
export function listRoles(query) {
  return request({
    url: "/chat/userRole/selectRolesByUserId",
    method: "get",
    params: query,
  });
}

// 查询详细
export function get(id) {
  return request({
    url: "/chat/user/" + id,
    method: "get",
  });
}

// 查询详细
export function showFace(openId) {
  return request({
    url: "/de/knowledgeBase/" + openId,
    method: "get",
  });
}

// 新增
export function add(data) {
  return request({
    url: "/de/knowledgeBase",
    method: "post",
    data: data,
  });
}

// 修改
export function update(data) {
  return request({
    url: "/de/knowledgeBase",
    method: "put",
    data: data,
  });
}

// 删除
export function del(id) {
  return request({
    url: "/de/knowledgeBase/" + id,
    method: "delete",
  });
}

// 删除知识库文件
export function delFile(query) {
  return request({
    url: `/de/knowledgeBase/removeFile/${query}`,
    method: "delete",
  });
}

/*-------------------------------------文档库-----------------------------------------*/

// 上传文档库文件
export function uploadFileDQ(data) {
  return request({
    url: "/de/knowledgeBase/uploadFile",
    method: "post",
    data: data
  });
}

// 文档库文件远程保存
export function saveFile(query) {
  return request({
    url: "/de/knowledgeBase/appendFileToKnowledgeBase",
    params: query,
  })
}

// 文档库 文件列表
export function fileListDQ(query) {
  return request({
    url: "/de/knowledgeBase/selectKbFileList",
    method: "get",
    params: query,
  })
}

// 文档库 文件删除
export function deleteFileDQ(ids) {
  return request({
    url: `/de/knowledgeBase/removeFile/${ids}`,
    method: "delete",
  })
}

/*----------------------------------------问答库----------------------------------------------*/

// 上传知识库文件
export function uploadFileWD(data) {
  return request({
    url: "/de/questionAnswer/upload",
    method: "post",
    data: data
  });
}

// 问答库文件列表
export function loadingFileRecord() {
  return request({
    url: "/de/questionFile/list",
  })
}

// 问答库 文件列表无分页
export function loadingFileRecordAll(query) {
  return request({
    url: "/de/questionFile/selectList",
    method: "get",
    params: query
  })
}

// 问答库 问答列表
export function listWD(query) {
  return request({
    url: "/de/questionAnswer/list",
    method: "get",
    params: query
  })
}


// 问答库 删除问答
export function deleteWD(ids) {
  return request({
    url: `/de/questionAnswer/removeQuestionAnswer/${ids}`,
    method: "delete",
  })
}

// 问答库 查询问答详情
export function selectWD(id) {
  return request({
    url: `/de/questionAnswer/${id}`,
    method: "get",
  })
}

// 问答库 新建问答
export function createWD(data) {
  return request({
    url: `/de/questionAnswer`,
    method: "post",
    data: data
  })
}

// 问答库 修改问答
export function updateWD(data) {
  return request({
    url: `/de/questionAnswer`,
    method: "put",
    data: data
  })
}

