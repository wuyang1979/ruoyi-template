import request from '@/utils/request'

// 查询待审核列表
export function listexpertWorks(query) {
    return request({
      url: '/thinkTank/expertWorks/draftList',
      method: 'get',
      params: query
    })
}
//   专家作品审批
  export function updetaexpertWorks(data) {
    return request({
      url: '/thinkTank/expertWorks/approval',
      method: 'post',
      data: data
    })
  }

  //   专家作品批量审批
  export function updetaWorks(data) {
    return request({
      url: '/thinkTank/expertWorks/batchApproval',
      method: 'post',
      data: data
    })
  }
  
// 查询专家作品详细
export function getexpertworks(id) {
  return request({
    url: '/thinkTank/expertWorks/' + id,
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
  