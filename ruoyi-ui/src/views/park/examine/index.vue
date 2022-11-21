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
      <el-form-item label="园区名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入园区名称"
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
          @click="resetparam('add0')"
          v-hasPermi="['park:management:batch']"
        >批量通过</el-button>
        <!-- <el-button
          :disabled="single"
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          @click="resetparam('att1')"
          v-hasPermi="['park:management:batch']"
        >批量驳回</el-button>-->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :show-overflow-tooltip="true" />

      <el-table-column
        label="园区名称"
        align="center"
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
      <el-table-column label="招商数量" align="center" prop="investmentNum" />
      <el-table-column label="所在地" align="center" prop="address" width="180" 
        :show-overflow-tooltip="true"
      
      />
      <el-table-column label="联系人" align="center" prop="contactPerson" width="180" />
      <el-table-column label="联系方式" align="center" prop="contactPhone" width="180" />
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
            v-hasPermi="['park:management:approval']"
            @click="handlePass(scope.row)"
          >审核通过</el-button>
          <el-button
            v-if="scope.row.status == '2'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleFail(scope.row)"
            v-hasPermi="['park:management:approval']"
          >审核不通过</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            v-hasPermi="['park:management:query']"
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
      class="view-dialog"
      width="1000px"
      append-to-body
    >
      <div style="height: 70vh;overflow: auto;">
        <div class="view-box">
          <div class="view-left">
            <div class="view-left-tip">
              <div>ABOUT</div>
              <div>{{form.name}}</div>
            </div>
            <div class="view-left-tab">
              <div @click="changeTab(1)">园区介绍</div>
              <div @click="changeTab(2)">入驻指南</div>
              <div @click="changeTab(3)">园区地址</div>
            </div>
          </div>
          <div class="view-right">
            <div v-if="currentTab == 1">
              <div class="right-title">园区介绍</div>
              <div v-html="form.content" class="cont-container"></div>
            </div>
            <div v-else-if="currentTab == 2">
              <div class="right-title">入驻指南</div>
              <div v-html="form.entryGuide" class="cont-container"></div>
            </div>
            <div v-else class="right-address">
              <div class="right-title">园区地址</div>
              <div>{{form.name}}</div>
              <div>地址：{{form.address}}</div>
              <div>联系电话：{{form.contactPhone}}</div>
              <div>
                <Map
                  :readOnly="true"
                  :latlng="{lat: form.latitude,lng: form.longitude}"
                  style="height: 500px;margin-top: 10px;"
                />
              </div>
            </div>
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
} from '@/api/park/examine'
import Map from '@/components/Map/index.vue'
export default {
  name: 'ParkExamine',
  components: { Map },
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
      // 预览tab
      currentTab: 1,
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
        province: null,
        city: null,
        region: null,
        town: null,
        address: null,
        longitude: null,
        latitude: null,
        investmentNum: null,
        typeId: null,
        contactPerson: null,
        contactPhone: null,
        auth: null,
        cover: null,
        videoUrl: null,
        introduce: null,
        content: null,
        entryGuide: null,
        recommend: '0',
        isTop: '0',
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
      this.form = row
      this.opensee = true
      this.title = '预览'
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
    changeTab(tab) {
      this.currentTab = tab
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 48px;
  width: 72px;
}
.view-dialog ::v-deep .el-dialog__body {
  padding: 0;
  .view-box {
    padding: 30px 20px;
    font-size: 16px;
    overflow: hidden;
    display: flex;
    .view-left {
      margin-right: 10px;
      flex: 0 0 180px;
      .view-left-tip {
        text-align: center;
        margin-bottom: 10px;
        padding: 0 10px;
        > div:first-child {
          color: #f7f7f7;
          font-weight: bold;
          font-size: 30px;
          letter-spacing: 4px;
          margin-bottom: 20px;
        }
        > div:last-child {
          font-size: 20px;
        }
      }
      .view-left-tab {
        font-size: 18px;
        > div {
          line-height: 60px;
          padding-left: 20px;
          cursor: pointer;
        }
        > div:hover {
          background-color: #1890ff;
          color: #fff;
        }
      }
    }
    .view-right {
      flex: 1;
      padding: 0 10px;
      .right-title {
        font-size: 18px;
        padding-left: 10px;
        line-height: 50px;
        border-bottom: 1px solid #333;
      }
      .cont-container {
        img {
          display: block;
          margin: auto;
          max-width: 100%;
        }
      }
      .right-address {
        > div:not(:last-child):not(:first-child) {
          margin-top: 16px;
        }
      }
    }
  }
}
</style>
