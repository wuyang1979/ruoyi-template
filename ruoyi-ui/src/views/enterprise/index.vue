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
      <el-form-item label="企业名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入企业名称"
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
          v-hasPermi="['enterprise:enterprise:add']"
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
          v-hasPermi="['enterprise:enterprise:remove']"
        >删除</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="enterpriseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        width="200"
        label="企业名称"
        align="left"
        prop="name"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            style="overflow: hidden; text-overflow: ellipsis; color: #44a5ff"
            @click="handleUpdate(scope.row, 'red')"
          >{{ scope.row.name }}</div>
        </template>
      </el-table-column>
      <el-table-column label="企业Logo" align="center" width="180" prop="logo">
        <template slot-scope="scope">
          <div v-if="scope.row.logo !== null">
            <el-image
              class="tablePic"
              :src="scope.row.logo[0]"
              :preview-src-list="scope.row.logo"
              alt
            />
          </div>
          <div v-if="scope.row.logo == null">
            <el-image class="tablePic" :src="morentup[0]" :preview-src-list="morentup" alt />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="统一信用代码"
        :show-overflow-tooltip="true"
        align="center"
        width="300"
        prop="creditCode"
      />
      <el-table-column
        label="企业性质"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="property"
      />
      <el-table-column
        label="所属行业"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="industryName"
      />
      <el-table-column
        label="企业类型"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="type"
      />
      <el-table-column
        label="法人"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="legalPerson"
      />
      <el-table-column
        label="联系方式"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="legalPersonPhone"
      />
      <el-table-column
        label="所在地区"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="cityName"
      />
      <el-table-column
        label="注册资金(万元)"
        :show-overflow-tooltip="true"
        align="center"
        width="180"
        prop="registerFund"
      />
      <el-table-column label="成立时间" align="center" width="180" prop="establishTime"></el-table-column>
      <el-table-column label="浏览量" align="center" width="120" prop="views" />
      <el-table-column label="收藏量" align="center" width="120" prop="collects" />
      <el-table-column label="点赞量" align="center" width="120" prop="favorites" />
      <el-table-column label="首页推荐" align="center" width="120" prop="isRecommend">
        <template slot-scope="scope">
          <el-switch
            @change="switchChange1(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            v-model="scope.row.isRecommend"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="置顶" align="center" width="120" prop="isTop">
        <template slot-scope="scope">
          <el-switch
            @change="switchChange2(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-value="1"
            inactive-value="0"
            v-model="scope.row.isTop"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="120" prop="statusValue" />
      <el-table-column
        label="操作"
        align="center"
        fixed="right"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-release"
            @click="handleUpdate(scope.row, 'red')"
            v-hasPermi="['enterprise:enterprise:query']"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="submitFaob(scope.row)"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['enterprise:enterprise:submit']"
          >提交审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-release"
            @click="handlefabu(scope.row, 'app')"
            v-hasPermi="['enterprise:enterprise:release']"
            v-if="scope.row.status == '3'"
          >发布</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-release"
            v-if="scope.row.status == '4'"
            @click="handleUnpublish(scope.row)"
            v-hasPermi="['enterprise:enterprise:cancel']"
          >取消发布</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row, 'upd')"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['enterprise:enterprise:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleJingying(scope.row)"
            v-hasPermi="['enterprise:management:list']"
          >经营情况</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleReject(scope.row)"
            v-if="scope.row.status == '2' || scope.row.status == '3'"
            v-hasPermi="['enterprise:enterprise:withdraw']"
          >撤回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="
              scope.row.status == '0' ||
              scope.row.status == '1' ||
              scope.row.status == '5'
            "
            v-hasPermi="['enterprise:enterprise:remove']"
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
    <!-- 企业经营情况弹框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="opensix"
      width="1000px"
      append-to-body
    >
      <el-table v-loading="loading" :data="formJingYing" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          width="120"
          label="填报年份"
          align="left"
          prop="year"
          :show-overflow-tooltip="true"
        />

        <el-table-column
          label="年营业额(元)"
          align="center"
          width="120"
          prop="yearTurnover"
          :show-overflow-tooltip="true"
        />

        <el-table-column
          label="平均毛利(元)"
          :show-overflow-tooltip="true"
          align="center"
          width="120"
          prop="grossMargin"
        />
        <el-table-column
          label="坏账率(%)"
          :show-overflow-tooltip="true"
          align="center"
          width="120"
          prop="badDebtRate"
        />
        <el-table-column
          label="年利率"
          :show-overflow-tooltip="true"
          align="center"
          width="120"
          prop="yearProfit"
        />
        <el-table-column
          label="备注"
          :show-overflow-tooltip="true"
          align="center"
          width="120"
          prop="remark"
        />
        <el-table-column
          label="填报者"
          :show-overflow-tooltip="true"
          align="center"
          width="120"
          prop="createBy"
        />
        <el-table-column
          label="创建时间"
          :show-overflow-tooltip="true"
          align="center"
          width="120"
          prop="createTime"
        />

        <el-table-column
          label="操作"
          align="center"
          fixed="right"
          width="200"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="tbjingying(scope.row, 'upd')"
              v-hasPermi="['enterprise:management:edit']"
            >修改</el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="managentDelete(scope.row)"
              v-hasPermi="['enterprise:management:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button
        type="primary"
        v-hasPermi="['enterprise:management:add']"
        @click="tbjingying(null, 'add')"
      >填报</el-button>
    </el-dialog>
    <!-- 添加或修改企业基础信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="900px"
      class="view-dialog"
      append-to-body
    >
      <div
        style="padding: 30px"
        :style="[
          { height: reading ? '70vh' : 'auto' },
          { overflow: reading ? 'auto' : '' },
        ]"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业名称" prop="name">
                <el-input
                  v-model="form.name"
                  placeholder="请输入企业名称"
                  maxlength="100"
                  show-word-limit
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="统一信用代码" prop="creditCode">
                <el-input v-model="form.creditCode" placeholder="请输入统一信用代码" :disabled="reading" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="注册资金(万元)" prop="registerFund">
                <el-input-number
                  v-model="form.registerFund"
                  placeholder="请输入注册资金"
                  :disabled="reading"
                  controls-position="right"
                  :min="0"
                ></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业官网" prop="officialWebsite">
                <el-input
                  v-model="form.officialWebsite"
                  placeholder="请输入企业官网"
                  maxlength="100"
                  show-word-limit
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="企业性质" prop="property">
                <el-select v-model="form.property" placeholder="请选择企业性质" :disabled="reading">
                  <el-option
                    v-for="item in natureOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属行业" prop="industryId">
                <TreeSelecter
                  v-if="!reading"
                  :props="treeSelectProps"
                  v-model="form.industryId"
                  :options="treeSelectOptions"
                  :value="form.industryId"
                ></TreeSelecter>
                <div v-if="reading">{{ form.industryName }}</div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="企业类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择企业类型" :disabled="reading">
                  <el-option
                    v-for="item in businessTypeOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="企业法人" prop="legalPerson">
                <el-input
                  v-model="form.legalPerson"
                  placeholder="请输入企业法人"
                  maxlength="50"
                  show-word-limit
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="legalPersonPhone">
                <el-input
                  v-model="form.legalPersonPhone"
                  placeholder="请输入联系电话"
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="法人代表" prop="legalRepresent">
                <el-input
                  v-model="form.legalRepresent"
                  placeholder="请输入法人代表"
                  maxlength="50"
                  show-word-limit
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="legalRepresentPhone">
                <el-input
                  v-model="form.legalRepresentPhone"
                  placeholder="请输入联系电话"
                  :disabled="reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="员工人数" prop="staffNumber">
                <el-input-number
                  v-model="form.staffNumber"
                  step-strictly
                  placeholder="请输入员工人数"
                  :disabled="reading"
                  controls-position="right"
                  :min="1"
                ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="成立时间" prop="establishTime">
                <el-date-picker
                  :disabled="reading"
                  v-model="form.establishTime"
                  type="date"
                  :picker-options="setDateRange"
                  placeholder="选择日期"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col style="display: flex" :span="24">
              <el-form-item label="所在地区" prop="province">
                <el-select
                  v-model="form.province"
                  @change="provinceChange"
                  placeholder="请选择省"
                  :disabled="reading"
                >
                  <el-option
                    v-for="item in provinceOptions"
                    :key="item.regionCode"
                    :label="item.regionName"
                    :value="item.regionCode"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="city">
                <el-select
                  v-model="form.city"
                  @change="cityChange"
                  placeholder="请选择市"
                  :disabled="reading"
                >
                  <el-option
                    v-for="item in cityOptions"
                    :key="item.regionCode"
                    :label="item.regionName"
                    :value="item.regionCode"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="region">
                <el-select v-model="form.region" placeholder="请选择区" :disabled="reading">
                  <el-option
                    v-for="item in regionOptions"
                    :key="item.regionCode"
                    :label="item.regionName"
                    :value="item.regionCode"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="经营范围" prop="businessScope">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入内容"
                  :disabled="reading"
                  maxlength="500"
                  show-word-limit
                  v-model="form.businessScope"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="主营产品" prop="mainProduct">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="请输入内容"
                  :disabled="reading"
                  maxlength="500"
                  show-word-limit
                  v-model="form.mainProduct"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业Logo" prop="logo">
                <upLoadPic
                  v-if="!reading"
                  :cover="form.logo"
                  @handleFileSuccess="handleFileSuccess1"
                  @handleRemove="handleRemove1"
                ></upLoadPic>
                <img class="tablePic" :src="form.logo" alt v-if="reading" />
                <div v-if="!reading">
                  <span>建议：上传图片尺寸为276*155，图片格式支持png、jpg、GIF、JPEG</span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="营业执照" prop="license">
                <upLoadPic
                  v-if="!reading"
                  :cover="form.license"
                  @handleFileSuccess="handleFileSuccess2"
                  @handleRemove="handleRemove2"
                ></upLoadPic>
                <img class="tablePic" :src="form.license" alt v-if="reading" />
                <div v-if="!reading">
                  <span>建议：上传图片尺寸为276*155，图片格式支持png、jpg、GIF、JPEG</span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="企业描述" prop="describing">
                <div
                  v-if="reading"
                  class="ql-editor"
                  v-html="form.describing"
                  style="border: 1px solid #ccc"
                ></div>
                <editor
                  @focus="focus($event)"
                  v-model="form.describing"
                  :min-height="192"
                  v-if="!reading"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="首页推荐" prop="isRecommend">
                <el-switch
                  :disabled="reading"
                  active-value="1"
                  inactive-value="0"
                  v-model="form.isRecommend"
                ></el-switch>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="置顶" prop="isTop">
                <el-switch
                  :disabled="reading"
                  active-value="1"
                  inactive-value="0"
                  v-model="form.isTop"
                ></el-switch>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!reading">
        <el-button type="primary" @click="submitForm('she')" :disabled="unAble">提交审核</el-button>
        <el-button type="primary" @click="submitForm('bao')">保存草稿</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      title="企业经营情况"
      :visible.sync="openJingYing"
      width="900px"
      append-to-body
    >
      <el-form ref="datajingying" :rules="rulesJingYing" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="填报年份">
              <el-date-picker
                v-model="datajingying.year"
                type="year"
                data="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="选择填报年份"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年营业额">
              <el-input-number
                v-model="datajingying.yearTurnover"
                placeholder="请输入年营业额"
                controls-position="right"
                :min="0"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="平均毛利">
              <el-input-number
                v-model="datajingying.grossMargin"
                placeholder="请输入平均毛利"
                controls-position="right"
                :min="0"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="坏账率" prop="badDebtRate">
              <el-input-number
                v-model="datajingying.badDebtRate"
                placeholder="请输入坏账率"
                controls-position="right"
                :min="0"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年利率">
              <el-input-number
                v-model="datajingying.yearProfit"
                placeholder="请输入年利率"
                controls-position="right"
                :min="0"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                type="textarea"
                :rows="2"
                v-model="datajingying.remark"
                maxlength="500"
                placeholder="备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitJingYingForm">确 定</el-button>
        <el-button @click="cancelJingYing">取 消</el-button>
      </div>
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
            <el-input v-model="remark" type="textarea" :placeholder="'请输入' + reason" />
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
  listEnterprise,
  getEnterprise,
  delEnterprise,
  delEnterp,
  addEnterprise,
  updateEnterprise,
  getIndustryNature,
  getIndustryTree,
  getIndustryType,
  getManagement,
  updateManagement,
  getRegion,
  fabuEnterprise,
  getment,
  postMangement,
  delManagement,
  submit,
  withdraw,
  recommendTop,
} from '@/api/enterprise/enterprise'
import { validURL } from '@/utils/validate.js'
import { validPhone } from '@/utils/wdcspValidate.js'
import upLoadPic from '../../components/upLoadPic'
import TreeSelecter from '../../components/treeSelect'

export default {
  name: 'Enterprise',
  components: { TreeSelecter, upLoadPic },
  data() {
    var checkCredit = (rule, value, cb) => {
      //  验证统一信用代码的正则表达式
      const reg = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/
      if (reg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的统一信用代码'))
    }
    var checkUrl = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      setTimeout(() => {
        if (!validURL(value)) {
          callback(new Error('请输入网址'))
        } else {
          callback()
        }
      }, 300)
    }
    var checkPhone = (rule, value, callback) => {
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
      setDateRange: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
      },
      morentup: [require('@/assets/images/profile.jpg')],
      reading: false,
      management: [],
      opensix: false,
      addoupd: '',
      idee: '',
      qiyeid: '',
      datajingying: {
        year: '',
        yearTurnover: undefined,
        grossMargin: undefined,
        remark: '',
        badDebtRate: undefined,
        legalPerson: '',
        legalPersonPhone: '',
        yearProfit: undefined,
      },
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
      // 企业基础信息表格数据
      enterpriseList: [],
      // 弹出层标题
      title: '',
      // 弹窗提交审核按钮是否可用
      unAble: true,
      // 是否显示弹出层
      open: false,
      openJingYing: false,
      showThreeInput: true,
      treeSelectProps: {
        value: 'id', // ID字段名
        label: 'typeName', // 显示名称
        // children: 'children'    // 子级字段名
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        industryId: null,
        creditCode: null,
        name: null,
        englishName: null,
        property: null,
        type: null,
        legalPerson: null,
        legalPersonPhone: null,
        legalRepresent: null,
        legalRepresentPhone: null,
        province: null,
        city: null,
        region: null,
        address: null,
        businessScope: null,
        officialWebsite: null,
        mainProduct: null,
        staffNumber: null,
        establishTime: null,
        registerFund: null,
        isTop: null,
        isSupplier: null,
        status: null,
        approveBy: null,
        approveTime: null,
        enterpriseService: null,
        views: null,
        collects: null,
        favorites: null,
        userId: null,
        deptId: null,
      },
      businessTypeOptions: [],
      natureOptions: [],
      industryOptions: [],
      provinceOptions: [],
      cityOptions: [],
      regionOptions: [],
      formJingYing: {},
      // 表单参数
      form: {
        name: '', //企业名称
        creditCode: '', //统一信用代码
        registerFund: undefined, //注册资金
        officialWebsite: '', //企业官网
        property: null, //企业性质
        industryId: null, //所属行业
        type: null, //企业类型
        legalPerson: '', //企业法人,
        legalPersonPhone: '', //联系方式
        legalRepresent: '', //法人代表
        legalRepresentPhone: '', //代表电话
        staffNumber: undefined, //员工人数
        establishTime: null, //成立时间
        province: null, //省
        city: null, //市
        region: null, //区
        businessScope: '', //经营范围
        mainProduct: '', //主营产品
        logo: null, //logo
        license: null, //营业执照
        describing: null, //企业描述
        isRecommend: '0', //首页推荐
        isTop: '0', //置顶
        views: null, //浏览量
        collects: null, //收藏量
        favorites: null, //点赞量
      },
      treeSelectOptions: [],
      // 表单校验
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
        industryId: [
          { required: true, message: '行业分类不能为空', trigger: 'change' },
        ],
        creditCode: [
          { required: true, message: '信用代码不能为空', trigger: 'blur' },
          { validator: checkCredit, trigger: 'change' },
        ],
        officialWebsite: [{ validator: checkUrl, trigger: 'change' }],
        legalPersonPhone: [{ validator: checkPhone, trigger: 'change' }],
        legalRepresentPhone: [{ validator: checkPhone, trigger: 'change' }],
      },
      rulesJingYing: {},
      currentId: '', // 操作行id
      remark: '', // 撤回，取消发布备注
      visible: false, // 撤回，取消发布弹窗
      rejectType: null, // 撤回or取消发布 0:取消发布 , 1:撤回
      reason: '', // 撤回or取消发布 text
    }
  },
  created() {
    this.getList()
    this.getIndustryNature()
    this.getIndustryTree()
    this.getIndustryType()
    this.getProvince()
    // this.provinceChange()
    // this.cityChange()
  },
  methods: {
    /** 查询企业基础信息列表 */
    getList() {
      this.loading = true
      listEnterprise(this.queryParams).then((response) => {
        this.enterpriseList = response.rows
        this.enterpriseList.forEach((element) => {
          if (element.logo !== null) {
            element.logo = element.logo.split(',')
          }
          if (element.status == '0') {
            element.approveBy == '草稿'
          } else if (element.status == '1') {
            element.approveBy == '退回'
          } else if (element.status == '2') {
            element.approveBy == '待审核'
          } else if (element.status == '3') {
            element.approveBy == '待发布'
          } else if (element.status == '4') {
            element.approveBy == '已发布'
          }
        })
        this.total = response.total
        this.loading = false
      })
    },

    // 企业经营情况修改按钮
    tbjingying(row, val) {
      if (val == 'upd') {
        const id = row.id
        this.idee = row.id
        this.addoupd = '1'
        getManagement(id).then((res) => {
          this.openJingYing = true
          this.datajingying = res.data
        })
      } else {
        this.title = '新增企业经营情况'
        this.addoupd = '0'
        this.openJingYing = true
      }
    },

    submitFaob(row) {
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
    switchChange1(n) {
      const add = {
        status: 0,
        id: n.id,
        isRecommend: n.isRecommend,
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
    getProvince() {
      getRegion(0).then((res) => {
        this.provinceOptions = res.data
      })
    },
    provinceChange() {
      getRegion(this.form.province).then((res) => {
        this.cityOptions = res.data
        this.form.city = null
        this.form.region = null
      })
    },
    cityChange() {
      getRegion(this.form.city).then((res) => {
        this.regionOptions = res.data
        this.form.region = null
      })
    },
    // 获取企业性质
    getIndustryNature() {
      getIndustryNature().then((res) => {
        this.natureOptions = res.data
      })
    },
    // 获取树形行业分类
    getIndustryTree() {
      getIndustryTree().then((res) => {
        this.treeSelectOptions = res.data
      })
    },
    // 获取企业类型
    getIndustryType() {
      getIndustryType().then((res) => {
        this.businessTypeOptions = res.data
      })
    },
    // 点击填报经营情况
    handleJingying(val) {
      this.qiyeid = val.id
      const add = {
        enterpriseId: this.qiyeid,
      }
      getment(add).then((res) => {
        this.formJingYing = res.rows
        this.opensix = true
        this.title = '经营情况'
      })
    },
    seejiyejingying() {
      const add = {
        enterpriseId: this.qiyeid,
      }
      getment(add).then((res) => {
        this.formJingYing = res.rows
        this.opensix = true
      })
    },
    // 经营情况取消按钮
    cancelJingYing() {
      this.openJingYing = false
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        name: '', //企业名称
        creditCode: '', //统一信用代码
        registerFund: undefined, //注册资金
        officialWebsite: '', //企业官网
        property: null, //企业性质
        industryId: null, //所属行业
        type: null, //企业类型
        legalPerson: '', //企业法人,
        legalPersonPhone: '', //联系方式
        legalRepresent: '', //法人代表
        legalRepresentPhone: '', //代表电话
        staffNumber: undefined, //员工人数
        establishTime: null, //成立时间
        province: null, //省
        city: null, //市
        region: null, //区
        businessScope: '', //经营范围
        mainProduct: '', //主营产品
        logo: null, //logo
        license: null, //营业执照
        describing: null, //企业描述
        isRecommend: '0', //首页推荐
        isTop: '0', //置顶
        views: null, //浏览量
        collects: null, //收藏量
        favorites: null, //点赞量
      }
      this.resetForm('form')
    },
    handleFileSuccess1(file) {
      this.form.logo = file.src
    },
    // 图片删除
    handleRemove1() {
      this.form.logo = ''
    },
    handleFileSuccess2(file) {
      this.form.license = file.src
    },
    // 图片删除
    handleRemove2() {
      this.form.license = ''
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
      this.unAble = false
      this.reset()
      this.open = true
      this.reading = false
      this.showThreeInput = false
      this.title = '添加企业基础信息'
    },
    /** 修改按钮操作 */
    handleUpdate(row, val) {
      if (row.status == '0' || row.status == '1' || row.status == '5') {
        this.unAble = false
      } else {
        this.unAble = true
      }

      if (val == 'red') {
        this.title = '预览'
        this.reading = true
      } else if (val == 'upd') {
        this.title = '修改企业基础信息'
        this.reading = false
      }
      this.reset()
      const id = row.id || this.ids
      getEnterprise(id).then((response) => {
        this.form = response.data
        getRegion(this.form.province).then((res) => {
          this.cityOptions = res.data
          this.form.city = response.data.city
          getRegion(this.form.city).then((r) => {
            this.regionOptions = r.data
            this.form.region = response.data.region
          })
        })
        this.open = true
        this.showThreeInput = true
      })
    },
    handleRelease(row) {},
    // 提交经营情况
    submitJingYingForm() {
      if (this.addoupd == '1') {
        const add = {
          id: this.idee,
          enterpriseId: this.qiyeid,
          badDebtRate: this.datajingying.badDebtRate,
          grossMargin: this.datajingying.grossMargin,
          remark: this.datajingying.remark,
          year: this.datajingying.year,
          yearProfit: this.datajingying.yearProfit,
          yearTurnover: this.datajingying.yearTurnover,
        }

        updateManagement(add).then(() => {
          this.seejiyejingying()
          this.openJingYing = false
          this.datajingying.badDebtRate = undefined
          this.datajingying.grossMargin = undefined
          this.datajingying.remark = ''
          this.datajingying.year = ''
          this.datajingying.yearProfit = undefined
          this.datajingying.yearTurnover = undefined
          this.$modal.msgSuccess('修改成功')
        })
      } else if (this.addoupd == '0') {
        const add = {
          enterpriseId: this.qiyeid,
          badDebtRate: this.datajingying.badDebtRate,
          grossMargin: this.datajingying.grossMargin,
          remark: this.datajingying.remark,
          year: this.datajingying.year,
          yearProfit: this.datajingying.yearProfit,
          yearTurnover: this.datajingying.yearTurnover,
        }

        return postMangement(add).then(() => {
          this.seejiyejingying()
          this.openJingYing = false
          this.datajingying.badDebtRate = undefined
          this.datajingying.grossMargin = undefined
          this.datajingying.remark = ''
          this.datajingying.year = ''
          this.datajingying.yearProfit = undefined
          this.datajingying.yearTurnover = undefined
          this.$modal.msgSuccess('新增成功')
        })
      }
    },
    // 发布按钮操作
    handlefabu(row, val) {
      const add = {
        ids: [row.id],
        status: 0,
      }
      fabuEnterprise(add).then((response) => {
        this.$modal.msgSuccess('成功')
        this.getList()
      })
    },
    // 退回按钮
    handleUnpublish(row) {
      this.currentId = row.id
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
    // 撤回提交
    submitReject() {
      switch (this.rejectType) {
        case 0:
          fabuEnterprise({
            ids: [this.currentId],
            status: 1,
            remark: this.remark,
          }).then((response) => {
            this.$modal.msgSuccess('取消发布成功')
            this.getList()
          })
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
    /** 提交按钮 */
    submitForm(val) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            if (val == 'she') {
              this.form.addStatus = '0'
            } else if (val == 'bao') {
              this.form.addStatus = ''
            }
            updateEnterprise(this.form).then((response) => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            if (val == 'she') {
              this.form.addStatus = '2'
            } else if (val == 'bao') {
              this.form.addStatus = '0'
            }

            addEnterprise(this.form).then((response) => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    // 删除企业经营情况
    managentDelete(row) {
      const ids = row.id || this.ids
      const add = {
        enterpriseId: this.qiyeid,
      }
      this.$modal
        .confirm('是否确认删除？')
        .then(function () {
          return delManagement(ids)
        })
        .then(() => {
          getment(add).then((res) => {
            this.formJingYing = res.rows
            this.opensix = true
            this.title = '经营情况'
          })
          this.$modal.msgSuccess('删除成功')
        })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除？')
        .then(function () {
          return delEnterprise(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
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
}
</style>
