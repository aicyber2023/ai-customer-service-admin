import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/de/sessionRecord/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id) {
  return request({
    url: '/de/sessionRecord/' + id,
    method: 'get'
  })
}

// 新增
export function add(data) {
  return request({
    url: '/de/sessionRecord',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/de/sessionRecord',
    method: 'put',
    data: data
  })
}

// 删除
export function del(id) {
  return request({
    url: '/de/sessionRecord/' + id,
    method: 'delete'
  })
}
