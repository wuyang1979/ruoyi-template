import request from '@/utils/request'

// 查询供需产品信息列表
export function listInfo(query) {
  return request({
    url: '/supply/info/list',
    method: 'get',
    params: query
  })
}


// 查询供需产品分类
export function fenleiInfo(query) {
  return request({
    url: '/supply/type/tree',
    method: 'get',
    params: query
  })
}

// 二维码接口
export function erweiiInfo(data) {
  return request({
    url: '/generateQr',
    method: 'post',
    data: data
  })
}
//产品图片
// export function uploadInfo(data) {
//   return request({
//     url: '/common/upload/img/',
//     method: 'post',
//     data: data
//   })
// }
/**
 * 上传图片
 * @param {"imgFile"://需要上传的文件} data
*/
export const uploadInfo = (files) => {
  // console.log(files,)
  // const formData = new FormData();
  // formData.append('files', files);
  // for (let i = 0; i < files.length; i++) { formData.append("files", files[i]); }
  return request({
    headers: {
      "Content-Type":"multipart/form-data"
    },
    url: '/common/uploads/',
    method: 'post',
    data: files
  })
}
// 查询所有企业
export function getEnterpriseList(files) {
  return request({
    url: '/supply/info/enterpriseList',
    method: 'post',
    data: files
  })
}
// 查询供需产品信息详细
export function getInfo(id) {
  return request({
    url: '/supply/info/' + id,
    method: 'get'
  })
}

// 新增供需产品信息
export function addInfo(data) {
  return request({
    url: '/supply/info',
    method: 'post',
    data: data
  })
}

// 发布、撤回供需产品信息
export function fabuInfo(data) {
  return request({
    url: '/supply/info/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 修改供需产品信息
export function updateInfo(data) {
  return request({
    url: '/supply/info',
    method: 'put',
    data: data
  })
}

// 删除供需产品信息
export function delInfo(id) {
  return request({
    url: '/supply/info/' + id,
    method: 'delete'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/supply/info/submitApproval',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/supply/info/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function recommendTop(data) {
  return request({
    url: '/supply/info/recommendTop',
    method: 'post',
    data: data
  })
}
