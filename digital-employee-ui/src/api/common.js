import request from '@/utils/request'

// 修改
export function upload(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data
  })
}

// 修改
export function uploads(data) {
  return request({
    url: '/common/uploads',
    method: 'post',
    data
  })
}
