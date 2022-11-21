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
          v-hasPermi="['park:management:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['park:management:remove']"
        >删除</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="园区名称"
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
      <el-table-column
        label="招商数量"
        align="center"
        prop="investmentNum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所在地"
        align="center"
        prop="address"
        width="180"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div>{{addressFormat(scope.row)}}</div>
        </template>
      </el-table-column>
      <el-table-column label="联系人" align="center" prop="contactPerson" width="180" />
      <el-table-column label="联系方式" align="center" prop="contactPhone" width="180" />
      <el-table-column label="浏览量" align="center" prop="views" width="120" />
      <el-table-column label="收藏量" align="center" prop="collects" width="120" />
      <el-table-column label="点赞量" align="center" prop="favorites" width="120" />
      <el-table-column label="首页推荐" align="center" prop="recommend" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recommend"
            @change="switchChange1(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column width="120" label="置顶" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isTop"
            @change="switchChange2(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="statusValue" width="120" />

      <el-table-column
        label="操作"
        align="center"
        width="300"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['park:management:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['park:management:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRelease(scope.row)"
            v-hasPermi="['park:management:release']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '4'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUnpublish(scope.row)"
            v-hasPermi="['park:management:cancel']"
          >取消发布</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleSubmit(scope.row)"
            v-if="scope.row.status == '0' || scope.row.status == '1' || scope.row.status == '5'"
            v-hasPermi="['park:management:submit']"
          >提交审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['park:management:withdraw']"
          >撤回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlesee(scope.row)"
            v-hasPermi="['park:management:query']"
          >预览</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改园区信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="1000px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="园区名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入园区名称" maxlength="100" show-word-limit />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12" class="address-row">
            <el-form-item label="所在地" prop="province">
              <el-select
                style="width: 110px"
                v-model="form.province"
                @change="provinceChange"
                filterable
                placeholder="请选择省"
              >
                <el-option
                  v-for="item in provinceOptions"
                  :key="item.regionCode"
                  :label="item.regionName"
                  :value="item.regionCode"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label prop="city" label-width="20px">
              <el-select
                style="width: 110px"
                v-model="form.city"
                @change="cityChange"
                filterable
                placeholder="请选择市"
              >
                <el-option
                  v-for="item in cityOptions"
                  :key="item.regionCode"
                  :label="item.regionName"
                  :value="item.regionCode"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label prop="region" label-width="20px">
              <el-select style="width: 110px" v-model="form.region" filterable placeholder="请选择区">
                <el-option
                  v-for="item in regionOptions"
                  :key="item.regionCode"
                  :label="item.regionName"
                  :value="item.regionCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="园区详细地址" prop="address">
              <el-input
                v-model="form.address"
                placeholder="请输入地址"
                maxlength="100"
                show-word-limit
                @change="changeAddress"
              >
                <el-button slot="append" type="primary" @click="getLatLng">获取经纬度</el-button>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="latlng">
          <span>经度：{{form.longitude}}</span>
          <span>纬度：{{form.latitude}}</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="招商数量" prop="investmentNum">
              <el-input v-model="form.investmentNum" placeholder="请输入招商数量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="typeId">
              <TreeSelecter
                :props="treeSelectProps"
                v-model="form.typeId"
                :options="treeSelectOptions"
                :value="form.typeId"
              ></TreeSelecter>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input
                v-model="form.contactPerson"
                placeholder="请输入联系人"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系方式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="作者" prop="auth">
              <el-input v-model="form.auth" placeholder="请输入作者" maxlength="50" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="园区宣传图" prop="cover">
          <upload-imgs :images="form.cover" @upload="uploadSuccess"></upload-imgs>
        </el-form-item>
        <el-form-item label="园区视频" prop="videoUrl">
          <upload-video :videos="videos" @change="getVideo"></upload-video>
        </el-form-item>
        <el-form-item label="简介" prop="introduce">
          <el-input
            v-model="form.introduce"
            type="textarea"
            rows="4"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="入驻指南">
          <editor v-model="form.entryGuide" :min-height="192" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="首页推荐" prop="recommend">
              <el-switch
                v-model="form.recommend"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="置顶" prop="isTop">
              <el-switch
                v-model="form.isTop"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            rows="4"
            placeholder="请输入内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('add')" :disabled="shenhe">提交审核</el-button>
        <el-button type="primary" @click="submitForm('att')">保存草稿</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="mapView"
      v-if="mapView"
      title="地图"
      width="1000px"
      append-to-body
    >
      <Map
        @setLatlng="setLatlng"
        :address="(provinceName?provinceName:'')+(cityName?cityName:'')+(regionName?regionName:'')+(form.address?form.address:'')"
        :latlng="{lat: form.latitude,lng: form.longitude}"
        style="height: 600px"
      />
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="visible"
      width="500px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-form :span="12">
          <el-form-item :label="reason">
            <el-input v-model="remark" type="textarea" :placeholder="'请输入'+reason" />
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReject">确 定</el-button>
        <el-button @click="cancelReject">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listManagement,
  getManagement,
  delManagement,
  addManagement,
  updateManagement,
  fabuManagement,
  fenleiInfo,
  getRegion,
  submit,
  withdraw,
  recommendTop,
} from '@/api/park/management'
import TreeSelecter from '@/components/treeSelect/index.vue'
import Map from '@/components/Map/index.vue'
import { validPhone } from '@/utils/wdcspValidate.js'
import UploadImgs from '@/components/UploadImgs'
import UploadVideo from '@/components/uploadVideo'

export default {
  name: 'ParkManagement',
  components: { TreeSelecter, Map, UploadImgs, UploadVideo },
  data() {
    let checkInt = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      if (Number(value) && value % 1 === 0 && value >= 0) {
        console.log(value)
        callback()
      } else {
        return callback(new Error('请输入正整数！'))
      }
    }

    var checkContactPhone = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      setTimeout(() => {
        if (!validPhone(value)) {
          callback(new Error('请输入电话号或手机号'))
        } else {
          callback()
        }
      }, 300)
    }
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
      // 园区信息表格数据
      managementList: [],
      // 预览判断条件
      opensee: false,
      // 弹窗提交审核按钮是否可用
      shenhe: true,
      // 是否修改详细地址
      isChangeAddress: false,
      // 地图弹窗
      mapView: false,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        investmentNum: [
          { validator: checkInt, trigger: 'blur', required: false },
        ],
        name: [
          { required: true, message: '园区名称不能为空', trigger: 'blur' },
        ],
        typeId: [
          { required: true, message: '分类不能为空', trigger: 'change' },
        ],
        contactPhone: [{ validator: checkContactPhone, trigger: 'blur' }],
        content: [
          { required: true, message: '产品内容不能为空', trigger: 'blur' },
        ],
        province: [
          { required: true, message: '省不能为空', trigger: 'change' },
        ],
        city: [{ required: true, message: '市不能为空', trigger: 'change' }],
        region: [{ required: true, message: '区不能为空', trigger: 'change' }],
        address: [
          {
            required: true,
            message: '地址不能为空',
            trigger: ['blur', 'change'],
          },
        ],
      },
      videos: [],
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      treeSelectOptions: [],
      // 省市区下拉数据
      provinceOptions: [],
      cityOptions: [],
      regionOptions: [],
      // 省市区名称-用于地图搜索
      provinceName: '',
      cityName: '',
      regionName: '',
      // 预览tab
      currentTab: 1,
      morentup: [require('@/assets/images/profile.jpg')],
      currentId: '', // 操作行id
      remark: '', // 撤回，取消发布备注
      visible: false, // 撤回，取消发布弹窗
      rejectType: null, // 撤回or取消发布 0:取消发布 , 1:撤回
      reason: '', // 撤回or取消发布 text
    }
  },
  watch: {
    'form.province'(val) {
      let str = this.provinceOptions.find((ele) => {
        return ele.regionCode == val
      })
      if (str) {
        this.provinceName = str.regionName
      } else {
        this.provinceName = ''
      }
    },
    'form.city'(val) {
      let str = this.cityOptions.find((ele) => {
        return ele.regionCode == val
      })
      if (str) {
        this.cityName = str.regionName
      } else {
        this.cityName = ''
      }
    },
    'form.region'(val) {
      let str = this.regionOptions.find((ele) => {
        return ele.regionCode == val
      })
      if (str) {
        this.regionName = str.regionName
      } else {
        this.regionName = ''
      }
    },
    'form.content'(val) {
      let reg = /^(\<p\>)\s+(\<\/p\>)$/g
      if (val == '<p><br></p>' || reg.test(val)) {
        this.form.content = ''
      }
    },
  },
  created() {
    this.getList()
    this.fenlei()
    this.getProvince()
  },
  methods: {
    addressFormat(row) {
      if (row.provinceValue.indexOf('市') != -1) {
        return row.provinceValue + row.regionValue
      } else {
        return row.provinceValue + row.cityValue + row.regionValue
      }
    },
    /** 查询园区信息列表 */
    getList() {
      this.loading = true
      listManagement(this.queryParams).then((response) => {
        this.managementList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 新增产品分类
    fenlei() {
      fenleiInfo(this.queryParams).then((res) => {
        this.treeSelectOptions = res.data
      })
    },
    // 发布按钮
    handleRelease(row) {
      this.currentId = row.id
      this.aastatused = '2'
      const id = row.id || this.ids
      this.submit()
    },
    // 退回按钮
    handleUnpublish(row) {
      this.currentId = row.id
      this.aastatused = '3'
      this.rejectType = 0
      this.visible = true
      this.title = '取消发布'
      this.reason = '取消原因'
    },
    // 撤回
    handleReject(row) {
      this.currentId = row.id
      this.rejectType = 1
      this.visible = true
      this.title = '撤回'
      this.reason = '撤回原因'
    },
    // 预览
    handlesee(row) {
      this.form = row
      this.opensee = true
      this.title = '预览'
      this.currentTab = 1
    },
    changeTab(tab) {
      this.currentTab = tab
    },
    switchChange1(n) {
      const add = {
        status: 0,
        id: n.id,
        recommend: n.recommend,
      }
      recommendTop(add).then((res) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
    },
    switchChange2(n) {
      const att = {
        status: 1,
        id: n.id,
        isTop: n.isTop,
      }
      recommendTop(att).then((response) => {
        this.$modal.msgSuccess('修改成功')
        this.getList()
      })
    },
    // 省
    getProvince() {
      getRegion(0).then((res) => {
        this.provinceOptions = res.data
      })
    },
    // 市
    provinceChange() {
      getRegion(this.form.province).then((res) => {
        this.cityOptions = res.data
        this.form.city = null
        this.form.region = null
      })
    },
    // 区
    cityChange() {
      getRegion(this.form.city).then((res) => {
        this.regionOptions = res.data
        this.form.region = null
      })
    },
    changeAddress(val) {
      this.isChangeAddress = true
    },
    getLatLng() {
      this.mapView = true
    },
    setLatlng(val) {
      this.mapView = false
      this.isChangeAddress = false
      this.form.longitude = val.lng
      this.form.latitude = val.lat
      this.form.address = val.address
    },
    uploadSuccess(val) {
      this.form.cover = val.join(',')
    },
    // 视频上传成功
    getVideo(event) {
      if (event && event.length > 0) {
        this.videos = event
        this.form.videoUrl = event.map((ele) => ele.url).join(',')
      } else {
        this.videos = []
        this.form.videoUrl = ''
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.isChangeAddress = false
      this.shenhe = false
      this.reset()
      this.open = true
      this.videos = []
      this.title = '添加园区信息'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.isChangeAddress = false
      if (row.status == '0' || row.status == '1' || row.status == '5') {
        this.shenhe = false
      } else {
        this.shenhe = true
      }
      this.reset()
      const id = row.id || this.ids
      getManagement(id).then((response) => {
        this.form = response.data
        let obj = {},
          arr = []
        if (this.form.videoUrl) {
          this.form.videoUrl.split(',').forEach((ele) => {
            obj.url = ele
            arr.push(obj)
          })
        }
        this.videos = arr
        this.open = true
        this.title = '修改园区信息'
        getRegion(this.form.province).then((res) => {
          this.cityOptions = res.data
          getRegion(this.form.city).then((r) => {
            this.regionOptions = r.data
          })
        })
      })
    },
    // 发布提交
    submit() {
      const add = {
        id: this.currentId,
        status: this.aastatused,
        remark: this.rejectType == 0 ? this.remark : null,
      }
      fabuManagement(add).then((response) => {
        if (add.status == '2') {
          this.$modal.msgSuccess('发布成功')
        } else {
          this.$modal.msgSuccess('取消发布成功')
        }
        this.getList()
      })
    },
    // 撤回提交
    submitReject() {
      switch (this.rejectType) {
        case 0:
          this.submit()
          break
        case 1:
          withdraw({
            id: this.currentId,
            remark: this.remark,
          }).then(() => {
            this.$modal.msgSuccess('撤回成功')
            this.getList()
          })
          break
      }
      this.cancelReject()
    },
    cancelReject() {
      this.visible = false
      this.reason = ''
      this.remark = ''
      this.rejectType = null
      this.currentId = ''
    },
    // 提交审核按钮操作
    handleSubmit(row) {
      const add = {
        id: row.id,
      }
      this.$modal
        .confirm('是否确认操作？')
        .then(function () {
          return submit(add)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('提交成功')
        })
    },
    /** 提交按钮 */
    submitForm(val) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.isChangeAddress) {
            return this.$modal.msgError('请获取经纬度信息')
          }
          if (this.form.id != null) {
            if (val == 'add') {
              this.form.addStatus = '0'
            } else {
              this.form.addStatus = ''
            }
            const add = this.form
            this.$modal
              .confirm('是否确认操作？')
              .then(function () {
                return updateManagement(add)
              })
              .then(() => {
                this.$modal.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
          } else {
            if (val == 'add') {
              this.form.addStatus = '2'
            } else {
              this.form.addStatus = '0'
            }
            const att = this.form
            this.$modal
              .confirm('是否确认操作？')
              .then(function () {
                return addManagement(att)
              })
              .then(() => {
                this.$modal.msgSuccess('添加成功')
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
        .confirm('是否确认删除该园区？')
        .then(function () {
          return delManagement(ids)
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
        'application/management/export',
        {
          ...this.queryParams,
        },
        `management_${new Date().getTime()}.xlsx`
      )
    },
  },
}
</script>
<style lang="scss" scoped>
.tablePic {
  height: 48px;
  width: 72px;
}
.address-row {
  display: flex;
}
::v-deep .el-input-group__append {
  background-color: #1890ff;
  color: #fff;
}
.latlng {
  padding-left: 120px;
  margin-bottom: 22px;
  color: #999;
  > span:not(:last-child) {
    margin-right: 20px;
  }
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
