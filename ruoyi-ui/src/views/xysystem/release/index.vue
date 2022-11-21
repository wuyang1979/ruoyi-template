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
      <!-- <el-form-item label="新闻类型id" prop="newsTypeId">
        <el-input
          v-model="queryParams.newsTypeId"
          placeholder="请输入新闻类型id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="新闻标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入新闻标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input
          v-model="queryParams.author"
          placeholder="请输入作者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核时间" prop="approveTime">
        <el-date-picker
          clearable
          v-model="queryParams.approveTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择审核时间"
        ></el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="来源" prop="source">
        <el-input
          v-model="queryParams.source"
          placeholder="请输入来源"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <!-- <el-form-item label="新闻分类" prop="classify">
        <el-input
          v-model="queryParams.classify"
          placeholder="请输入新闻分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-input
          v-model="queryParams.cover"
          placeholder="请输入封面"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="推荐" prop="recommend">
        <el-input
          v-model="queryParams.recommend"
          placeholder="请输入推荐"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览量" prop="views">
        <el-input
          v-model="queryParams.views"
          placeholder="请输入浏览量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收藏量" prop="collects">
        <el-input
          v-model="queryParams.collects"
          placeholder="请输入收藏量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点赞量" prop="favorites">
        <el-input
          v-model="queryParams.favorites"
          placeholder="请输入点赞量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="置顶" prop="isTop">
        <el-input
          v-model="queryParams.isTop"
          placeholder="请输入置顶"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核人" prop="approveBy">
        <el-input
          v-model="queryParams.approveBy"
          placeholder="请输入审核人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门id" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入部门id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:news:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:news:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:news:remove']"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:news:export']"
          >导出</el-button
        >
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="newsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="新闻表id" align="center" prop="id" :show-overflow-tooltip="true"/> -->
      <!-- <el-table-column label="新闻类型id" align="center" prop="newsTypeId" :show-overflow-tooltip="true"/> -->
      <el-table-column label="新闻标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="新闻内容" align="center" prop="content" :show-overflow-tooltip="true"/> -->
      <el-table-column label="作者" align="center" prop="author" :show-overflow-tooltip="true" />
      <el-table-column label="来源" align="center" prop="source" :show-overflow-tooltip="true" />
      <el-table-column label="新闻类型" align="center" prop="typeName" :show-overflow-tooltip="true" />
      <el-table-column label="新闻分类" align="center" prop="classify" :show-overflow-tooltip="true" />
      <el-table-column label="封面" align="center" prop="cover" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <img class="tablePic" :src="scope.row.cover" alt />
        </template>
      </el-table-column>
      <el-table-column label="简介" align="center" prop="introduction" :show-overflow-tooltip="true" />

      <el-table-column label="浏览量" align="center" prop="views" :show-overflow-tooltip="true" />
      <el-table-column label="收藏量" align="center" prop="collects" :show-overflow-tooltip="true" />
      <el-table-column label="点赞量" align="center" prop="favorites" :show-overflow-tooltip="true" />
      <el-table-column label="推荐" align="center" prop="recommend" :show-overflow-tooltip="true">
        <template slot-scope="scope">{{scope.row.recommend==="0"?'否':'是'}}</template>
      </el-table-column>
      <el-table-column label="置顶" align="center" prop="isTop" :show-overflow-tooltip="true">
        <template slot-scope="scope">{{scope.row.isTop==="0"?'否':'是'}}</template>
      </el-table-column>
      <!-- <el-table-column
        label="状态"
        align="center"
        prop="status"
        :show-overflow-tooltip="true"
      />-->
      <!-- <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/> -->
      <!-- <el-table-column label="审核人" align="center" prop="approveBy" :show-overflow-tooltip="true"/> -->
      <!-- <el-table-column label="审核时间" align="center" prop="approveTime" width="180" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.approveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <!-- <el-table-column label="用户id" align="center" prop="userId" :show-overflow-tooltip="true"/> -->
      <!-- <el-table-column label="部门id" align="center" prop="deptId" :show-overflow-tooltip="true"/> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:news:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:news:remove']"
          >删除</el-button>
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

    <!-- 添加或修改新闻动态对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="新闻标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入新闻标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="来源" prop="source">
              <el-input v-model="form.source" placeholder="请输入来源" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="新闻类型" prop="newsTypeId">
              <TreeSelecter
                :props="treeSelectProps"
                v-model="form.newsTypeId"
                :options="treeSelectOptions"
                :value="form.newsTypeId"
              ></TreeSelecter>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="新闻分类" prop="classify">
              <el-input v-model="form.classify" placeholder="请输入新闻分类" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面" prop="cover">
              <upLoadPic
                :cover="form.cover"
                @handleFileSuccess="handleFileSuccess"
                @handleRemove="handleRemove"
              ></upLoadPic>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="简介" prop="introduction">
              <el-input v-model="form.introduction" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" maxlength="500" show-word-limit/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="置顶" prop="isTop">
              <el-select v-model="form.isTop" placeholder="请选择">
                <el-option
                  v-for="item in yesNoOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="推荐" prop="recommend">
              <el-select v-model="form.recommend" placeholder="请选择">
                <el-option
                  v-for="item in yesNoOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="showThreeInput">
          <el-col :span="12">
            <el-form-item label="点赞量" prop="favorites">
              <el-input :disabled="true" v-model="form.favorites" placeholder="请输入点赞量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收藏量" prop="collects">
              <el-input :disabled="true" v-model="form.collects" placeholder="请输入收藏量" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="showThreeInput">
          <el-col :span="12">
            <el-form-item label="浏览量" prop="views">
              <el-input :disabled="true" v-model="form.views" placeholder="请输入浏览量" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="新闻内容">
              <editor v-model="form.content" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listNews,
  getNews,
  delNews,
  addNews,
  updateNews,
  typeTreeNews,
} from '@/api/xysystem/news'
import TreeSelecter from '../../../components/treeSelect/index.vue'
import upLoadPic from '../../../components/upLoadPic'
export default {
  name: 'News',
  components: { TreeSelecter, upLoadPic },
  data() {
    return {
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
      newsList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      showThreeInput: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        newsTypeId: null,
        title: null,
        content: null,
        author: null,
        source: null,
        type: null,
        classify: null,
        cover: null,
        introduction: null,
        recommend: null,
        views: null,
        collects: null,
        favorites: null,
        isTop: null,
        status: null,
        approveBy: null,
        approveTime: null,
        userId: null,
        deptId: null,
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
    // this.getTypeTreeNews();
  },
  methods: {
    /** 查询新闻动态列表 */
    getList() {
      this.loading = true
      listNews(this.queryParams).then((response) => {
        this.newsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询新闻类型（树） */
    // getTypeTreeNews() {
    //   typeTreeNews().then((res) => {
    //     this.treeSelectOptions = res.data
    //   });
    // },
    // 取消按钮
    cancel() {
      this.open = false
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.showThreeInput = false
      this.title = '添加新闻动态'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getNews(id).then((response) => {
        this.form = response.data
        this.open = true
        this.showThreeInput = true
        this.title = '修改新闻动态'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateNews(this.form).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addNews(this.form).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除新闻动态编号为"' + ids + '"的数据项？')
        .then(function () {
          return delNews(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'system/news/export',
        {
          ...this.queryParams,
        },
        `news_${new Date().getTime()}.xlsx`
      )
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 40px;
  width: 70px;
}
</style>
