<template>
  <div>
    <div ref="lineBarChart" class="line-bar-chart" />
  </div>
</template>

<script>
import * as echarts from "echarts";
import Moment from "moment";
import { extendMoment } from "moment-range";
import { queryOrderSum } from "@/api/homePage/order.js";

const moment = extendMoment(Moment);

export default {
  data() {
    return {
      // ? 默认时间为最近30天
      time: [moment().subtract(29, "days"), moment().toDate()],
      totalPriceChart: null,
    };
  },
  mounted() {
    this.totalPriceChart = echarts.init(this.$refs.lineBarChart);
    this.getData();
  },
  beforeDestroy() {
    this.totalPriceChart.dispose(), (this.totalPriceChart = null);
  },
  methods: {
    async getData() {
      const res = await queryOrderSum({});
      console.log("res: 累计金额", res);
      const handleSum = res.data;
      const dateRange = moment.range(
        new Date(this.time[0]),
        new Date(this.time[1])
      );
      console.log("dateRange: ", dateRange);
      let days = Array.from(dateRange.by("day"));
      console.log("days: ", days);
      days = days.map((m) => m.format("YYYY-MM-DD"));
      console.log("days: ", days);
      // ? 构建serie数据
      const count = days.map((day) => {
        // ? 如果存在,代表有单
        if (handleSum[day]) {
          return handleSum[day];
        }
        return 0;
      });
      console.log("count: ", count);
      this.totalPriceChart.setOption({
        title: {
          text: "累计订单金额",
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
        xAxis: [
          {
            type: "category",
            data: days,
            axisPointer: {
              type: "shadow",
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            min: 0,
            max: 250,
            interval: 50,
          },
          {
            type: "value",
            min: 0,
            max: 500000,
            interval: 50000,
          },
        ],
        series: [
          {
            name: "需求订单",
            type: "bar",
            barWidth: 15,
            itemStyle: {
              color: "#a1cdfb",
            },
            data: [0,0,0,168,0,0,0,120,0,0,0,50,0,0,170,0,80,0,0,0,200,0,0,50, 0, 140, 0, 150, 0, 55],
          },
          {
            name: "handle品订单",
            type: "line",
            itemStyle: {
              color: "#e89e42",
            },
            smooth: true,
            yAxisIndex: 1,
            data: count,
          },
        ],
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.line-bar-chart {
  width: 100%;
  height: 400px;
}
</style>