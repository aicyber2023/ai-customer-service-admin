import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/de/bizUser/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id) {
  return request({
    url: '/de/bizUser/' + id,
    method: 'get'
  })
}

// 新增
export function add(data) {
  return request({
    url: '/de/bizUser',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data) {
  return request({
    url: '/de/bizUser',
    method: 'put',
    data: data
  })
}

// 删除
export function del(id) {
  return request({
    url: '/de/bizUser/' + id,
    method: 'delete'
  })
}

//重置密码
export function updatePwd(query) {
  return request({
    url: '/de/bizUser/updatePwd',
    method: 'get',
    params: query,
  })
}
