import request from '@/utils/request'


export function listNews(query) {
    return request({
      url: '/xyNews/news/draftList',
      method: 'get',
      params: query
    })
}
//   新闻审批
  export function updetaNews(data) {
    return request({
      url: '/xyNews/news/approval',
      method: 'post',
      data: data
    })
  }

  //   新闻审批
  export function updetaPlNews(data) {
    return request({
      url: '/xyNews/news/batchApproval ',
      method: 'post',
      data: data
    })
  }
  

  // 查询新闻类型
// export function typeNews() {
//     return request({
//       url: '/xyNews/type/list',
//       method: 'get'
//     })
//   }
  // 查询新闻类型（树）
//   export function typeTreeNews() {
//     return request({
//       url: '/xyNews/type/tree',
//       method: 'get'
//     })
//   }
  // 查询新闻动态详细
  export function getNews(id) {
    return request({
      url: '/xyNews/news/' + id,
      method: 'get'
    })
  }
  
  // 新增新闻动态
//   export function addNews(data) {
//     return request({
//       url: '/xyNews/news/approval',
//       method: 'post',
//       data: data
//     })
//   }
  // 修改新闻动态
//   export function updateNews(data) {
//     return request({
//       url: '/xyNews/news',
//       method: 'put',
//       data: data
//     })
//   }
  
  // 删除新闻动态
//   export function delNews(id) {
//     return request({
//       url: '/xyNews/news/' + id,
//       method: 'delete'
//     })
//   }
  