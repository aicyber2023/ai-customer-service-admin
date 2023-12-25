import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/de/digitalEmployee/list',
    method: 'get',
    params: query
  })
}

// 查询列表
export function listAll(query) {
  return request({
    url: '/de/digitalEmployee/selectList',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id) {
  return request({
    url: '/de/digitalEmployee/' + id,
    method: 'get'
  })
}

// 新增
export function add(data) {
  return request({
    url: '/de/digitalEmployee',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/de/digitalEmployee',
    method: 'put',
    data: data
  })
}

// 删除
export function del(id) {
  return request({
    url: '/de/digitalEmployee/' + id,
    method: 'delete'
  })
}

// 资源
export function getResource() {
  return request({
    url: '/de/workbench/chatResource',
    method: 'get'
  })
}

// 工作台查询全部员工
export function getAllEmployee() {
  return request({
    url: "/de/workbench/digitalEmployee",
    method: "get"
  })
}


