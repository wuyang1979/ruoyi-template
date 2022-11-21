<template>
  <div class="upload-box">
    <div class="avatar-uploader-box">
      <!-- 图片预览 -->
      <div :key="index" class="video-preview" v-for="(item, index) in videoList">
        <video :src="item.url" @mouseover.stop="item.isShowPopup = true" class="avatar">您的浏览器不支持视频播放</video>
        <!-- 显示查看和删除的按钮弹窗 -->
        <div
          @mouseleave="item.isShowPopup = false"
          class="avatar-uploader-popup"
          v-show="item.url && item.isShowPopup"
        >
          <i @click="previewVideo(item)" class="el-icon-zoom-in"></i>
          <i @click="deleteVideo(index)" class="el-icon-delete"></i>
        </div>
      </div>

      <!-- 方框样式 -->
      <el-upload
        action="#"
        :auto-upload="false"
        :on-change="handleAvatarChange"
        :show-file-list="false"
        class="avatar-uploader"
        ref="avatarUploader"
        v-show="uploadShow"
      >
        <span
          element-loading-background="rgba(0, 0, 0, 0.8)"
          element-loading-spinner="el-icon-loading"
          element-loading-text="上传中"
          style="display:block;"
          v-loading="loading"
        >
          <i class="el-icon-plus avatar-uploader-icon"></i>
        </span>
      </el-upload>

      <!-- 上传提示文字样式 -->
      <div class="upload-tip">
        <slot>建议：视频格式支持MP4</slot>
      </div>
    </div>
    <!-- 查看大图 -->
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      append-to-body
      center
      title="视频查看"
      :before-close="handleClose"
    >
      <video :src="videoSrc" ref="video" controls alt width="100%">您的浏览器不支持视频播放</video>
    </el-dialog>
  </div>
</template>

<script>
import { uploadvideos } from '@/api/upload'
export default {
  name: 'UploadVideo',
  components: {},
  props: {
    videos: {
      type: Array,
      default: function () {
        return []
      },
    },
    numLimit: {
      // 最大允许上传个数
      type: Number,
      default: 1,
    },
    sizeLimit: {
      // 最大单文件大小
      type: Number,
      default: 50,
    },
  },
  data() {
    return {
      loading: false,
      dialogVisible: false,
      videoList: [],
      videoSrc: '',
    }
  },
  computed: {
    uploadShow() {
      return this.videoList.length < this.numLimit
    },
  },
  watch: {
    videos: {
      handler() {
        const isArray = Array.isArray(this.videos)
        if (isArray && this.videos.length > 0) {
          this.videos.map((item) => {
            item.isShowPopup = false
          })
          this.videoList = JSON.parse(JSON.stringify(this.videos))
        } else {
          this.videoList = []
        }
      },
      deep: true,
      immediate: true,
    },
  },
  created() {},
  methods: {
    // 上传之前
    beforeAvatarUpload(file) {
      return new Promise((resolve, reject) => {
        if (
          [
            'video/mp4',
            'video/ogg',
            'video/flv',
            'video/avi',
            'video/wmv',
            'video/rmvb',
            'video/mov',
          ].indexOf(file.raw.type) == -1
        ) {
          // eslint-disable-next-line prefer-promise-reject-errors
          return reject('请上传正确的视频格式!')
        }

        if (file.size / 1024 / 1024 > this.sizeLimit) {
          // eslint-disable-next-line prefer-promise-reject-errors
          return reject(`视频大小不能超过${this.sizeLimit}M!`)
        }

        if (this.videoList.length === this.numLimit) {
          // eslint-disable-next-line prefer-promise-reject-errors
          return reject(`最多上传${this.numLimit}个视频`)
        }

        resolve('符合表单规则')
      })
    },
    // 上传改变
    async handleAvatarChange(file, fileList) {
      try {
        await this.beforeAvatarUpload(file)
        this.uploadImgApi(file)
      } catch (e) {
        this.$message.warning(JSON.stringify(e))
      }
    },
    // 上传视频准备
    uploadImgApi(data) {
      uploadvideos(data.raw).then((res) => {
        if (res.videoUrl) {
          this.videoList.push({
            url: res.videoUrl,
            isShowPopup: false,
          })
          this.$emit('change', this.videoList)
        }
      })
    },
    // 预览视频
    previewVideo(data) {
      this.videoSrc = data.url
      this.dialogVisible = true
    },
    // 删除视频
    deleteVideo(index) {
      this.videoList.splice(index, 1)
      this.$emit('change', this.videoList)
    },
    handleClose() {
      const video = document.getElementsByTagName('video')[1]
      if (!video.paused) {
        video.currentTime = 0
        video.pause()
      }
      this.dialogVisible = false
    },
  },
}
</script>

<style lang="scss">
$width: 150px;
$height: 150px;

.upload-box {
  .avatar-uploader-box {
    position: relative;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    padding-bottom: 20px;
    min-width: 350px;
    .avatar-uploader {
      .el-upload {
        width: $width;
        height: $height;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        &:hover {
          border-color: #409eff;
        }
        .el-loading-spinner {
          width: $width;
          height: $height;
          position: relative;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          margin-top: 0;
          top: 0;
          .el-loading-text {
            margin: 0;
          }
        }
      }
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: $width;
        height: $height;
        line-height: $height;
        text-align: center;
      }
    }
    .video-preview {
      position: relative;
      padding-right: 20px;
      .avatar {
        width: $width;
        height: $height;
        display: block;
        border-radius: 6px;
        margin-bottom: 13px;
      }
      .avatar-uploader-popup {
        position: absolute;
        top: 0;
        left: 0;
        z-index: 2;
        width: $width;
        height: $height;
        background: rgba($color: #000000, $alpha: 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        color: #fff;
        font-size: 20px;
        border-radius: 6px;
        i {
          width: 30%;
          text-align: center;
          padding: 0 5%;
          font-size: 24px;
        }
      }
    }
    .upload-tip {
      position: absolute;
      bottom: 0;
      left: 0;
      font-size: 12px;
      color: #606266;
      max-width: 350px;
      line-height: 20px;
    }
  }
}
</style>
