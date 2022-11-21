import request from '@/utils/request'

// 查询新闻动态列表
export function listNews(query) {
  return request({
    url: '/xyNews/news/list',
    method: 'get',
    params: query
  })
}
// 查询新闻类型
// export function typeNews() {
//   return request({
//     url: '/xyNews/type/list',
//     method: 'get'
//   })
// }
// 查询新闻类型
export function typeNews() {
  return request({
    url: '/xyNews/type/type',
    method: 'get'
  })
}
// 查询新闻类型（树）
export function typeTreeNews() {
  return request({
    url: '/xyNews/type/tree',
    method: 'get'
  })
}
// 查询新闻动态详细
export function getNews(id) {
  return request({
    url: '/xyNews/news/' + id,
    method: 'get'
  })
}

// 新增新闻动态
export function addNews(data) {
  return request({
    url: '/xyNews/news',
    method: 'post',
    data: data
  })
}

// 修改新闻动态
export function updateNews(data) {
  return request({
    url: '/xyNews/news',
    method: 'put',
    data: data
  })
}

// 删除新闻动态
export function delNews(id) {
  return request({
    url: '/xyNews/news/' + id,
    method: 'delete'
  })
}

export function fabuNews(data) {
  return request({
    url: '/xyNews/news/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/xyNews/news/submit',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/xyNews/news/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function recommendTop(data) {
  return request({
    url: '/xyNews/news/recommendTop',
    method: 'post',
    data: data
  })
}
