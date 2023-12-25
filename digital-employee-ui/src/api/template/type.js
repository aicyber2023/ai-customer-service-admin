import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/chat/templateType/list',
    method: 'get',
    params: query
  })
}

// 查询列表
export function listAll(query) {
  return request({
    url: '/chat/templateType/selectList',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id) {
  return request({
    url: '/chat/templateType/' + id,
    method: 'get'
  })
}

// 新增
export function add(data) {
  return request({
    url: '/chat/templateType',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/chat/templateType',
    method: 'put',
    data: data
  })
}

// 删除
export function del(id) {
  return request({
    url: '/chat/templateType/' + id,
    method: 'delete'
  })
}
