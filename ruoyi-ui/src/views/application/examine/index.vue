<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="应用名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入应用名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属企业" prop="enterpriseBelong">
        <el-input
          v-model="queryParams.enterpriseBelong"
          placeholder="请输入所属企业"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          :disabled="single"
          type="success"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['application:management:batch']"
          @click="resetparam('add0')"
        >批量通过</el-button>
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          v-hasPermi="['application:management:batch']"
          @click="resetparam('att1')"
        >批量驳回</el-button>-->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />

      <el-table-column
        label="应用名称"
        align="left"
        prop="name"
        width="300"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handlesee(scope.row)"
          >{{ scope.row.name }}</div>
        </template>
      </el-table-column>
      <el-table-column
        label="Handle码"
        width="300"
        align="left"
        prop="handleCode"
        style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUrl(scope.row)"
          >
            <span>{{ scope.row.handleCode }}</span>
            <el-image
              style="width: 10px; height: 10px;visibility: hidden;"
              :src="scope.row.handleQrUrl"
              :ref="'listPreview'+scope.row.handleCode"
              fit="contain"
              :preview-src-list="[scope.row.handleQrUrl]"
            ></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="封面图" align="center" prop="cover" width="120">
        <template slot-scope="scope">
          <div v-if="scope.row.cover == null || scope.row.cover == ''">
            <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
          </div>
          <div v-else>
            <el-image
              class="tablePic"
              :src="scope.row.cover?scope.row.cover.split(',')[0]:''"
              alt
              :preview-src-list="scope.row.cover?scope.row.cover.split(','):[]"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="typeName" width="120" />
      <el-table-column
        label="所属企业"
        align="center"
        prop="enterpriseBelong"
        width="180"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="扫码下载" align="center" prop="downloadQrUrl" width="200">
        <template slot-scope="scope">
          <div>
            <el-image
              class="tableQr"
              :src="scope.row.downloadQrUrl"
              alt
              :preview-src-list="[scope.row.downloadQrUrl]"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="作者" align="center" prop="auth" width="120" />
      <el-table-column label="下载量" align="center" prop="download" width="120" />
      <el-table-column label="浏览量" align="center" prop="views" width="120" />
      <el-table-column label="收藏量" align="center" prop="collects" width="120" />
      <el-table-column label="点赞量" align="center" prop="favorites" width="120" />
      <el-table-column
        label="推荐"
        width="120"
        align="center"
        prop="recommend"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">{{ scope.row.recommend === "0" ? "否" : "是" }}</template>
      </el-table-column>
      <el-table-column label="置顶" align="center" prop="isTop" :show-overflow-tooltip="true">
        <template slot-scope="scope">{{ scope.row.isTop === "0" ? "否" : "是" }}</template>
      </el-table-column>
      <el-table-column
        label="状态"
        align="center"
        width="120"
        prop="statusValue"
        :show-overflow-tooltip="true"
      ></el-table-column>

      <el-table-column
        label="操作"
        fixed="right"
        width="300"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == '2'"
            size="mini"
            type="text"
            icon="el-icon-check"
            v-hasPermi="['application:management:approval']"
            @click="handlePass(scope.row)"
          >审核通过</el-button>
          <el-button
            v-if="scope.row.status == '2'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleFail(scope.row)"
            v-hasPermi="['application:management:approval']"
          >审核不通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            v-hasPermi="['application:management:query']"
            @click="handlesee(scope.row)"
          >预览</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看新闻动态对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opensee"
      v-if="opensee"
      class="view-dialog"
      width="1000px"
      append-to-body
    >
      <div style="height: 70vh;overflow: auto;">
        <div class="view-box">
          <div class="view-head">
            <div class="head-left">
              <el-carousel
                ref="carousel"
                v-if="form.cover == null || form.cover == ''"
                height="150px"
                style="background: #f7f7f7"
              >
                <el-carousel-item v-for="item in morentup" :key="item">
                  <el-image :src="item" fit="contain" style="width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
              <el-carousel ref="carousel" v-else height="150px" style="background: #f7f7f7">
                <el-carousel-item v-for="item in form.cover" :key="item">
                  <el-image :src="item" fit="contain" style="width: 100%; height: 100%" />
                </el-carousel-item>
              </el-carousel>
            </div>
            <div class="head-right">
              <div class="head-right-top">
                <div class="head-right-top-left">
                  <div>{{form.name}}</div>
                  <div>分类：{{form.typeName}}</div>
                  <div>公司名称：{{form.enterpriseBelong}}</div>
                </div>
                <div class="head-right-top-right">
                  <div class="self-button" @click="download(1)">
                    <span>点击下载应用</span>
                  </div>
                  <div class="self-button download-btn" @click="download(2)">
                    <span>扫 码 下 载</span>
                    <el-image
                      style="width: 26px; height: 26px;margin-left: 10px"
                      :src="form.downloadQrUrl"
                      ref="preview"
                      fit="contain"
                      :preview-src-list="[form.downloadQrUrl]"
                    ></el-image>
                  </div>
                </div>
              </div>
              <div class="head-right-bot">Handle码：{{form.handleCode}}</div>
            </div>
          </div>
          <div class="view-introduce">
            <div class="introduce-title">应用介绍</div>
            <div v-html="form.content"></div>
          </div>
        </div>
      </div>
    </el-dialog>
    <!--新闻审核审核弹框-->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opentwo"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12">
          <el-form-item label="驳回原因">
            <el-input v-model="queryParams.bohui" type="textarea" placeholder="请输入驳回原因" />
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="canceltwo">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listManagement,
  getManagement,
  approval,
  batchApproval,
} from '@/api/application/examine'
export default {
  name: 'ApplicationExamine',
  data() {
    return {
      opensee: false,
      opentwo: false,
      opensrc: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 新闻动态表格数据
      managementList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      id: '',
      showThreeInput: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        enterpriseBelong: null,
        bohui: '',
      },
      yesNoOptions: [
        {
          value: '0',
          label: '否',
        },
        {
          value: '1',
          label: '是',
        },
      ],
      treeSelectOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      rules: {},
      morentup: [require('@/assets/images/profile.jpg')],
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询新闻动态列表 */
    getList() {
      this.loading = true
      listManagement(this.queryParams).then((response) => {
        this.managementList = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    // 取消按钮
    canceltwo() {
      this.opentwo = false
      this.reset()
    },
    handleFileSuccess(file) {
      this.form.cover = file.src
    },
    // 图片删除
    handleRemove() {
      this.form.cover = ''
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        enterpriseBelong: null,
        contactPerson: null,
        contactPhone: null,
        typeId: null,
        auth: null,
        downloadUrl: null,
        handleCode: null,
        cover: null,
        videoUrl: null,
        introduce: null,
        content: null,
        recommend: '0',
        isTop: '0',
        download: null,
        views: null,
        collects: null,
        favorites: null,
        remark: null,
        status: '0',
        delFlag: null,
        publishTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        userId: null,
        deptId: null,
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 批量通过/驳回
    resetparam(val) {
      let abb = ''
      if (val == 'add0') {
        abb = '0'
      } else {
        abb = '1'
      }
      const add = {
        ids: this.ids,
        status: abb,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return batchApproval(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.getList()
          this.single = true
        })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      if (selection.length !== '0') {
        this.single = false
      }

      this.multiple = !selection.length
    },
    /** 审核按钮操作 */
    handleFail(row) {
      this.reset()
      const id = row.id || this.ids
      this.id = row.id
      this.opentwo = true
      this.title = '驳回'
    },
    handlesee(row) {
      this.form = { ...row }
      this.form.cover = this.form.cover ? this.form.cover.split(',') : []
      this.opensee = true
      this.title = '预览'
    },
    handleUrl(row) {
      this.$refs['listPreview' + row.handleCode].showViewer = true
    },
    // 审核提交
    submit() {
      const add = {
        status: '1',
        remark: this.queryParams.bohui,
        id: this.id,
      }
      approval(add).then((response) => {
        this.$modal.msgSuccess('审核成功')
        this.opentwo = false
        this.queryParams.bohui = ''
        this.getList()
      })
    },

    /** 通过按钮操作 */
    handlePass(row) {
      const add = {
        status: '2',
        reason: this.queryParams.bohui,
        id: row.id,
      }
      this.$modal
        .confirm('是否确认通过')
        .then(function () {
          return approval(add)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('审核成功')
        })
        .catch(() => {})
    },
    download(index) {
      if (index == 1) {
        window.open(this.form.downloadUrl)
      } else {
        this.$refs.preview.showViewer = true
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 48px;
  width: 72px;
}
.tableQr {
  height: 48px;
  width: 48px;
}

.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
  .view-box {
    font-size: 16px;
    .view-head {
      padding: 30px;
      display: flex;
      justify-content: space-between;
      .head-left {
        width: 33%;
      }
      .head-right {
        width: 64%;
        .head-right-top {
          display: flex;
          justify-content: space-between;
          .head-right-top-left > div {
            margin-top: 15px;
          }
          .head-right-top-right > div {
            margin-top: 15px;
          }
          .head-right-top-right {
            margin-right: 40px;
            .self-button {
              background: #1890ff;
              height: 40px;
              line-height: 40px;
              border-radius: 4px;
              width: 200px;
              text-align: center;
              cursor: pointer;
              span {
                color: #fff;
                display: inline-block;
                width: 100px;
                font-size: 14px;
              }
            }
            .download-btn {
              display: flex;
              align-items: center;
              span {
                margin-left: 50px;
                letter-spacing: 2.4px;
              }
            }
          }
        }
        .head-right-bot {
          color: #999;
          margin-top: 15px;
        }
      }
    }
    .view-introduce {
      padding: 0 30px 20px;
      .introduce-title {
        font-size: 18px;
        padding-bottom: 20px;
        border-bottom: 1px solid #999;
      }
      img {
        display: block;
        margin: auto;
        max-width: 100%;
      }
    }
  }
}
</style>
