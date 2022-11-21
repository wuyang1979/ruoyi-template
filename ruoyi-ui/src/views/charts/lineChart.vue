<template>
  <div>
    <div class="line-chart" ref="lineChart"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import Moment from "moment";
import { extendMoment } from "moment-range";
import {queryUserActivity} from '@/api/homePage/activity.js'

const moment = extendMoment(Moment);

export default {
  data() {
    return {
      activeChart: null,
       // ? 默认时间为最近30天
      time: [moment().subtract(29, "days"), moment().toDate()],
    };
  },
  mounted() {
    this.getData()
    this.activeChart = echarts.init(this.$refs.lineChart);
    
  },
  methods:{
async getData(){
  const res =await queryUserActivity({})
  console.log('res: 用户活跃度', res);
  const userAct=res.data;
  const dateRange = moment.range(
        new Date(this.time[0]),
        new Date(this.time[1])
      );
  
      let days = Array.from(dateRange.by("day"));

      days = days.map((m) => m.format("YYYY-MM-DD"));

      // ? 构建serie数据
      const count = days.map((day) => {
        // ? 如果存在,代表有单
        if (userAct[day]) {
          return userAct[day];
        }
        return 0;
      });
      console.log("count:活跃度数据 ", count);
      const option = {
        title: {
        text: "用户活跃度",
        x: "center",
        y: "7px",
        textStyle: {
          color: "#555555",
          fontSize: 16,
        },
        textAlign: "left",
      },
      tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            crossStyle: {
              color: "#999",
            },
          },
        },
      xAxis: {
        type: "category",
        data: days,
      },
      yAxis: {
        
          type: "value",
          min: 0,
          max: 100,
          interval: 10,
        
      },
      series: [
        {
         data: count,
          type: "line",
          itemStyle:{
            color:'#e89e42'
          },
          smooth: true,
        },
      ],
    };
    this.activeChart.setOption(option);
}
  },
  beforeDestroy() {
    this.activeChart.dispose(), (this.activeChart = null);
  },
};
</script>

<style lang="scss" scoped>
.line-chart {
  width: 100%;
  height: 400px;
}
</style>