import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/de/product/list',
    method: 'get',
    params: query
  })
}

// 查询列表
export function listAll(query) {
  return request({
    url: '/de/product/selectList',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id) {
  return request({
    url: '/de/product/' + id,
    method: 'get'
  })
}

// 新增
export function add(data) {
  return request({
    url: '/de/product',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/de/product',
    method: 'put',
    data: data
  })
}

// 删除
export function del(id) {
  return request({
    url: '/de/product/' + id,
    method: 'delete'
  })
}
