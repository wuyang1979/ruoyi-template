// 政策审核


<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="96px"
    >
      <el-form-item label="所属企业名称" prop="enterpriseName">
        <el-input
          v-model="queryParams.enterpriseName"
          placeholder="请输入所属企业名称"
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
          @click="resetparam('0')"
          v-hasPermi="['thinkTank:technology:batch']"
        >批量通过</el-button>
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          @click="resetparam('1')"
          v-hasPermi="['thinkTank:technology:batch']"
        >批量驳回</el-button> -->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="technologyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />
      <el-table-column
        width="300"
        label="标题"
        align="left"
        prop="title"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUpdate(scope.row)"
          >{{ scope.row.title }}</div>
        </template>
      </el-table-column>
      <el-table-column
        label="封面"
        align="center"
        prop="cover"
        width="120"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.coverType == '1' || scope.row.coverType == '3'">
            <el-image
              class="tablePic"
              :src="scope.row.cover[0]"
              alt
              :preview-src-list="scope.row.cover"
            />
          </div>
          <div v-if="scope.row.coverType == '0' || scope.row.coverType == null">
            <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="所属企业名称"
        align="center"
        prop="enterpriseName"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="分类"
        align="center"
        prop="typeName"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="来源"
        align="center"
        prop="source"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="作者"
        align="center"
        prop="author"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="浏览量"
        align="center"
        prop="views"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="收藏量"
        align="center"
        prop="collects"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="点赞量" align="center" prop="favorites" :show-overflow-tooltip="true" />
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
            size="mini"
            type="text"
            icon="el-icon-check"
            v-hasPermi="['thinkTank:technology:approval']"
            @click="handletongguo(scope.row)"
          >审核通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleexamine(scope.row)"
            v-hasPermi="['thinkTank:technology:approval']"
          >审核不通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            v-hasPermi="['thinkTank:technology:query']"
            @click="handleUpdate(scope.row)"
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
      :visible.sync="opensee"
      width="900px"
      class="dialog"
      append-to-body
      :title="title"
    >
      <div style="height: 70vh;overflow:auto;padding: 30px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="formll">
          <el-row>
            <div class="biaoti">{{ form.title }}</div>
          </el-row>
          <el-row>
            <el-col style="display: flex; flex-direction: row; margin-left: 15px">
              <span>{{ form.author }}；</span>
              <span>{{ form.createTime }}；</span>
              <span>{{ form.source }}</span>
            </el-col>
          </el-row>
          <el-divider content-position="right"></el-divider>
          <el-row>
            <el-col>
              <!-- <div class="sketch_content"> -->
              <div class="ql-editor" v-html="form.content"></div>
              <!-- </div> -->
            </el-col>
          </el-row>
          <div class="laiyuan">来源：{{ form.source }}</div>
        </el-form>
      </div>
    </el-dialog>

    <!--审核弹框-->
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
  listpolicyReview,
  updetapolicyReview,
  updetapolicyReviews,
  getTechnology,
} from '@/api/xy-think-tank/technicalReview'
import TreeSelecter from '../../../components/treeSelect/index.vue'
import upLoadPic from '../../../components/upLoadPic'
export default {
  name: 'News',
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
      morentup: [require('@/assets/images/profile.jpg')],
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
      technologyList: [],
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
        enterpriseName: '',
        radio: '',
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
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询新闻动态列表 */
    getList() {
      this.loading = true
      listpolicyReview(this.queryParams).then((response) => {
        this.technologyList = response.rows
        this.technologyList.forEach((element) => {
          if (element.cover !== null) {
            element.cover = element.cover.split(',')
          }
        })

        this.total = response.total
        this.loading = false
      })
    },
    // 默认图片
    srcsee() {
      this.opensrc = true
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
        newsTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        type: null,
        classify: null,
        cover: '',
        introduction: null,
        recommend: '0',
        views: null,
        collects: null,
        favorites: null,
        isTop: '0',
        status: '0',
        remark: null,
        approveBy: null,
        approveTime: null,
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
      const add = {
        ids: this.ids,
        status: val,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return updetapolicyReviews(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.getList()
        })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      console.log(this.ids)
      if (selection.length !== '0') {
        this.single = false
      }
      this.multiple = !selection.length
    },
    /** 审核按钮操作 */
    handleexamine(row) {
      console.log(row)
      this.reset()
      const id = row.id || this.ids
      this.id = row.id
      this.opentwo = true
      this.title = '驳回'
    },
    /** 预览按钮操作 */
    handleUpdate(row) {
      console.log(row)
      this.reset()
      const id = row.id || this.ids
      getTechnology(id).then((response) => {
        this.form = response.data
        this.form.content = this.form.content.replace(
          /<img/g,
          "<img style='max-width:90%;height:auto;'"
        )
        this.opensee = true
        this.showThreeInput = true
        this.title = '预览'
      })
    },
    submitsee() {
      this.opensee = false
    },
    // 审核提交
    submit() {
      const add = {
        status: '1',
        remark: this.queryParams.bohui,
        id: this.id,
      }
      this.$modal
        .confirm('是否确认操作')
        .then(function () {
          return updetapolicyReview(add)
        })
        .then(() => {
          this.$modal.msgSuccess('审核成功')
          this.opentwo = false
          this.queryParams.radio = ''
          this.queryParams.bohui = ''
          this.getList()
        })
    },

    /** 通过按钮操作 */
    handletongguo(row) {
      const add = {
        status: '2',
        remark: this.queryParams.bohui,
        id: row.id,
      }
      this.$modal
        .confirm('是否确认通过')
        .then(function () {
          return updetapolicyReview(add)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('审核成功')
        })
        .catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.sketch_content {
  // 必须有高度 overflow 为自动
  overflow: auto;
  height: 434px;
  border-top: 1px solid #eff1f4;
  border-bottom: 1px solid #eff1f4;
  padding: 0px 30px 11px 27px;

  // 滚动条的样式,宽高分别对应横竖滚动条的尺寸
  &::-webkit-scrollbar {
    width: 3px;
  }

  // 滚动条里面默认的小方块,自定义样式
  &::-webkit-scrollbar-thumb {
    background: #8798af;
    border-radius: 2px;
  }

  // 滚动条里面的轨道
  &::-webkit-scrollbar-track {
    background: transparent;
  }
}
.morensrc {
  width: 100%;
  height: 100%;
}
.laiyuan {
  position: relative;
  left: 11px;
}
.biaoti {
  margin-left: 10px;
  margin-bottom: 30px;
  font-size: 36px;
}
.formll {
  margin-top: 30px;
}
.dialog {
  position: absolute;
}
.totol {
  color: #ffffffff;
  padding-top: 10px;
  margin: 0;
  padding-left: 10px;
  font-size: 18px;
  width: 100%;
  height: 50px;
  background-color: #006efffd;
  top: 0px;
  left: 0px;
  position: absolute;
  margin-bottom: 30px;
  .closse {
    margin-top: 5px;
    margin-right: 10px;
    float: right;
  }
}
.tablePic {
  height: 40px;
  width: 70px;
}
.dialog ::v-deep .el-dialog__body {
  padding: 0;
}
</style>
