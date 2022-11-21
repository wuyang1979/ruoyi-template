<template>
  <div>
    <el-upload
      :ref="`Uploader-${uploadId}`"
      v-loading="loading"
      action="upload"
      :style="style"
      :class="{hide:fileList.length >= limit}"
      :auto-upload="false"
      list-type="picture-card"
      :show-file-list="showFileList"
      :name="uploadId"
      :accept="accept"
      multiple
      :disabled="disabled"
      :file-list="fileList"
      :limit="limit"
      :on-exceed="handleExceed"
      :before-upload="handleBeforeUpload"
      :http-request="httpRequest"
      :on-change="handleChange"
      :on-remove="handleRemove"
      :on-preview="handlePictureCardPreview"
    >
      <i class="el-icon-plus" />
      <div class="el-upload__tip" slot="tip">建议：最多上传{{limit}}张图片，上传图片尺寸为276*155，图片格式支持{{accept}}</div>
    </el-upload>
    <el-dialog
      :close-on-click-modal="false"
      title="图片预览"
      :visible.sync="dialogVisible"
      append-to-body
    >
      <img width="100%" :src="dialogImageUrl" alt />
    </el-dialog>
  </div>
</template>
<script>
import { uploadInfos } from '@/api/upload'
export default {
  name: 'AppImagesUploader',
  model: {
    event: 'upload',
    prop: 'images',
  },
  props: {
    disabled: {
      //是否禁用
      type: [Boolean],
      required: false,
      default: false,
    },
    name: {
      //上传字段的name
      type: [String],
      required: false,
      default: 'files',
    },

    size: {
      //中间的加号大小
      type: [String],
      required: false,
      default: '28px',
    },
    w: {
      //宽
      type: [String],
      required: false,
      default: 'auto',
    },
    h: {
      //高
      type: [String],
      required: false,
      default: 'auto',
    },
    images: {
      //图片数组
      type: [String],
      required: false,
      default() {
        return ''
      },
    },
    limit: {
      //图片数量
      type: [Number],
      required: false,
      default() {
        return 5
      },
    },
    fileSize: {
      //图片大小限制(MB)
      type: [Number],
      required: false,
      default() {
        return 4
      },
    },
    accept: {
      //接受上传的文件类型
      type: [String],
      required: false,
      default() {
        return '.png, .jpg, .GIF, .JPEG'
      },
    },
  },
  data() {
    return {
      uploadId: Math.random().toString(36).substr(2).toLocaleUpperCase(),
      loading: false,
      showFileList: false,
      fileList: [],
      fm: new FormData(),
      uploadFiles: [], //待上传的文件列表
      fileTotal: 0, //上传文件总数
      dialogImageUrl: '',
      dialogVisible: false,
    }
  },
  computed: {
    style() {
      const { w, h, size } = this
      return {
        width: w,
        height: h,
        fontSize: size,
      }
    },
  },

  watch: {
    images: {
      handler(newVal, oldVal) {
        if (newVal === '' || newVal === null || !newVal) {
          this.fileList = []
          this.showFileList = false
        } else {
          this.fileList = this.images.split(',').map((url) => ({ url }))
          this.showFileList = true
        }
        console.log(this.fileList)
      },
      immediate: true,
      deep: true,
    },
  },

  created() {},

  methods: {
    // 超出limit
    handleExceed(files, fileList) {
      this.$modal.msgError(
        `当前限制选择 ${this.limit} 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      )
    },
    /**
     * 手动上传回调
     */
    async httpRequest(file) {
      this.fm.append(this.name, file.file)
      //当fm getall的数组长度与filetotal的长度一致，就可以上传了
      if (this.fm.getAll(this.name).length === this.fileTotal) {
        try {
          //上传方法
          const data = await uploadInfos(this.fm)
          //这里是上传成功之后要做的事情
          console.log(data)
          if (data.urls) {
            this.showFileList = true
            let urls = data.urls.split(',')
            if (this.fileList.length) {
              for (const index in urls) {
                this.fileList.push({
                  url: urls[index],
                })
              }
            } else {
              this.fileList = urls.map((item) => ({
                url: item,
              }))
            }
            this.$emit(
              'upload',
              this.fileList.map((item) => item.url)
            )
          }
        } catch (error) {
          console.log(`上传文件出错`, error)
        } finally {
          //无论成功与失败都要清空文件列表，否则会出现无法上传的情况
          this.uploadFiles = []
          this.fm.delete(this.name)
          this.loading = false
        }
      }
    },

    /**
     * 上传前回调
     */
    handleBeforeUpload(file) {
      console.log(file)
      const isSize = file.size / 1024 / 1024 < this.fileSize
      this.loading = true
      if (!isSize) {
        this.$modal.msgError(`上传图片大小不能超过 ${this.fileSize} MB!`)
        this.fileTotal = this.fileTotal - 1
      }
      if (!isSize) this.loading = false
      return isSize
    },

    /**
     * 文件上传change
     */
    handleChange(file, fileList) {
      console.log(file)
      //获取添加文件进来的状态
      file.status == 'ready' && this.uploadFiles.push(file.raw)
      this.showFileList = false
      //获取原始文件的个数
      this.fileTotal = document.getElementsByName(this.uploadId)[0].files.length
      //如果原始文件和upload的个数相同的时候就说明已经添加完了，可以触发上传的方法了
      if (this.uploadFiles.length === this.fileTotal) {
        //获取上传文件的组件
        const Uploader = this.$refs[`Uploader-${this.uploadId}`]
        //触发上传文件列表的方法
        Uploader.submit()
      }
    },

    /**
     * 移除图片回调
     */
    handleRemove(file, fileList) {
      const index = this.fileList.findIndex((item) => item.uid == file.uid)
      if (index != -1) {
        this.fileList.splice(index, 1)
        this.$emit(
          'upload',
          this.fileList.map((item) => item.url)
        )
      }
      if (file.status !== 'success') {
        this.uploadFiles = []
        this.loading = false
        if (this.fileList.length > 0) {
          this.showFileList = true
        }
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
  },
}
</script>
<style lang="scss">
.el-upload-list__item {
  transition: none !important;
}
</style>
<style lang="scss" scoped>
.hide {
  ::v-deep .el-upload--picture-card {
    display: none;
  }
}
.image-uploader {
  ::v-deep .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    background: #fff;
    img {
      display: inline-block;
      width: 100%;
    }
  }
}
</style>
