<template>
  <div class="home">
    <div class="total-data">
      <p>
        <span>平台新增/总量</span>
      </p>
      <div class="outer-container_one">
        <div class="data-container">
          <p>
            <span> 应用数量 </span>
          </p>

          <div class="num_data">{{platformData.applicationCount}}</div>
        </div>
        <div class="data-container">
          <p>
            <span> handle品 </span>
          </p>

          <div class="num_data">{{platformData.handleCount}}</div>
        </div>
        <div class="data-container">
          <p>
            <span> 企业数 </span>
          </p>

          <div class="num_data">{{platformData.enterpriseCount}}</div>
        </div>
      </div>
      <div class="outer-container_two">
        <div class="data-container">
          <p>
            <span> 政策数量 </span>
          </p>

          <div class="num_data">{{platformData.policyCount}}</div>
        </div>
        <div class="data-container">
          <p>
            <span> 专家数 </span>
          </p>

          <div class="num_data">{{platformData.expertCount}}</div>
        </div>
        <div class="data-container">
          <p>
            <span> 技术内容 </span>
          </p>

          <div class="num_data">{{platformData.technologyCount}}</div>
        </div>
      </div>
    </div>
    <div class="chart-container">
      <p>
        <span>运营数据</span>
      </p>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="12" :lg="12">
          <div class="chart-wrapper">
            <lineBarChart></lineBarChart>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="12">
          <div class="chart-wrapper">
            <orderPieChart></orderPieChart>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="32">
        <el-col :xs="24" :sm="12" :lg="12">
          <div class="chart-wrapper">
            <activeChart></activeChart>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="12">
          <div class="chart-wrapper">
            <catePie></catePie>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryPlatformData } from "@/api/homePage/getNum.js";
import orderPieChart from "@/views/charts/orderPieChart";
import lineBarChart from "@/views/charts/lineBarChart";
import activeChart from "@/views/charts/lineChart";
import catePie from "@/views/charts/catePie";
export default {
  name: "Index",
  components: {
    orderPieChart,
    lineBarChart,
    activeChart,
    catePie,
  },
  data() {
    return {
      platformData: {},// 平台数据
    };
  },
  created() {
    this.loadPlatformData();
  },

  methods: {
    // ? 获取平台新增/总量
    async loadPlatformData() {
      const res = await queryPlatformData({});
      console.log("res:平台数据 ", res);
      this.platformData=res.data
    },
    
  },
};
</script>

<style scoped lang="scss">
.home {
  padding: 32px;
  width: 100%;
  // height: 100vh;
  // overflow: auto;
}
.total-data p span {
  font-size: 18px;
  font-weight: 800;
  color: #333333;
}
.outer-container_one {
  width: calc(100%-100px);
  display: flex;
  justify-content: space-between;
  margin-left: 60px;
  margin-top: 30px;
}
.outer-container_two {
  width: calc(100%-100px);
  display: flex;
  justify-content: space-between;
  margin-left: 60px;
  margin-top: 65px;
}
.data-container {
  width: 315px;
  height: 138px;
  border: 1px solid #4a94f7;
  border-radius: 5px;
}
.data-container p span {
  font-size: 16px;
  font-weight: 700;
  color: #555555;
  margin-left: 32px;
}
.num_data {
  width: fit-content;
  height: fit-content;
  font-size: 48px;
  font-weight: 700;
  text-align: #333333;
  margin: auto;
}
.chart-container {
  margin-top: 80px;
}
.chart-container p span {
  font-size: 18px;
  font-weight: 800;
  color: #333333;
}
// .chart-wrapper {
//     background: #fff;
//     padding: 16px 16px 0;
//     margin-bottom: 32px;
//   }
</style>

