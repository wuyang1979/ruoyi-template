import request from '@/utils/request'

/**
 * 上传多张图片
 * @param {"imgFile"://需要上传的文件} data
*/
export const uploadInfos = (files) => {
  return request({
    headers: {
      "Content-Type":"multipart/form-data"
    },
    url: '/common/uploads/',
    method: 'post',
    data: files
  })
}


/**
* 上传视频
* @param {"imgFile"://需要上传的文件} data
*/
export const uploadvideos = (file) => {
 const formData = new FormData();
 formData.append('file', file);

 return request({
   headers: {
     "Content-Type":"multipart/form-data"
   },
   url: '/common/uploadVideo',
   method: 'post',
   data: formData
 })
}
