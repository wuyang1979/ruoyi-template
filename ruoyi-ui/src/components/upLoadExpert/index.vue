<template>
  <div>
    <el-upload
      v-if="showUpload"
      :headers="uploadHeaders"
      :action="uploadUrl"
      :on-success="handleFileSuccess"
      :show-file-list="false"
      accept=".jpg, .png, .bmp, .svg"
    >
      <i class="el-icon-plus bigUpload"></i>
    </el-upload>
    <div v-if="!showUpload" class="showImg">
      <img width="100%" :src="url" alt @click="showImg" />
      <el-tooltip class="item" effect="dark" content="删除图片" placement="top">
        <div class="delImg" @click="handleRemove" v-if="!showUpload">×</div>
      </el-tooltip>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      width="850px"
      :visible.sync="dialogVisible"
      append-to-body
    >
      <img width="100%" class="showBigImg" :src="url" alt />
    </el-dialog>
  </div>
</template>
<script>
import { getToken } from '@/utils/auth'
export default {
  name: 'upLoadPic',
  props: {
    cover: {
      type: String,
      default: () => {
        return ''
      },
    },
  },
  data() {
    return {
      showUpload: true,
      dialogVisible: false,
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload/img',
      uploadHeaders: {
        Authorization: 'Bearer ' + getToken(),
      },
      url: '',
    }
  },
  mounted() {},
  methods: {
    handleFileSuccess(file) {
      this.$emit('handleFileSuccess', file)
      this.showUpload = false
    },
    // 图片删除
    handleRemove(file, fileList) {
      this.$emit('handleRemove', '')
      this.showUpload = true
    },
    // 图片预览
    showImg() {
      this.dialogVisible = true
    },
  },
  watch: {
    cover: {
      handler(newVal, oldVal) {
        this.url = newVal
        if (this.url === '' || this.url === null) {
          this.showUpload = true
        } else {
          this.showUpload = false
        }
      },
      immediate: true,
    },
  },
}
</script>
<style lang="scss" scoped>
.bigUpload::v-deep {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px !important;
  width: 140px !important;
  font-size: 28px !important;
  border: 1px solid #ccc;
}
.showImg {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px !important;
  width: 140px !important;
  border: 1px dashed #ccc;
  position: relative;
  cursor: pointer;
  .delImg {
    position: absolute;
    right: -10px;
    top: -10px;
    height: 20px;
    width: 20px;
    background-color: red;
    font-size: 16px;
    color: #fff;
    line-height: 20px;
    text-align: center;
    border-radius: 10px;
    cursor: pointer;
  }
}
.showBigImg {
  height: 400px;
  width: 710px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
