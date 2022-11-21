import request from '@/utils/request'

// 查询待审核列表
export function listpolicyReview(query) {
    return request({
      url: '/thinkTank/policy/draftList',
      method: 'get',
      params: query
    })
}
//   政策审批
  export function updetapolicyReview(data) {
    return request({
      url: '/thinkTank/policy/approval',
      method: 'post',
      data: data
    })
  }

  //   政策批量审批
  export function updetapolicyReviews(data) {
    return request({
      url: '/thinkTank/policy/batchApproval ',
      method: 'post',
      data: data
    })
  }
  
// 查询政策库详细
export function getPolicy(id) {
  return request({
    url: '/thinkTank/policy/' + id,
    method: 'get'
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
  