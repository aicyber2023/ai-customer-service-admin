import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/de/employeeTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询列表
export function listAll(query) {
  return request({
    url: '/de/employeeTemplate/selectList',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id) {
  return request({
    url: '/de/employeeTemplate/' + id,
    method: 'get'
  })
}

// 新增
export function add(data) {
  return request({
    url: '/de/employeeTemplate',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/de/employeeTemplate',
    method: 'put',
    data: data
  })
}

export function updateStatus(data) {
  return request({
    url: '/de/employeeTemplate/updateTemplateStatus',
    method: 'get',
    params: data
  })
}
// 删除
export function del(id) {
  return request({
    url: '/de/employeeTemplate/' + id,
    method: 'delete'
  })
}

// 上传数字员工头像
export function uploadFace(data) {
  return request({
    url: '/de/employeeTemplate/uploadAvatar',
    method: 'post',
    data
  })
}

// 上传公司Logo
export function uploadLogo(data) {
  return request({
    url: '/de/employeeTemplate/uploadCompanyAvatar',
    method: 'post',
    data
  })
}


// 查询正在使用的数字员工模板
export function selectUsedTemplateList() {
  return request({
    url: "/de/employeeTemplate/selectUsedTemplateList",
    method: "get"
  })
}
