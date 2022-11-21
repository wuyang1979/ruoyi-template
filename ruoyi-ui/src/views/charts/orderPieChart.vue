<template>
  <div>
    <div ref="pieChartOne" class="bar-chart-one" />
  </div>
</template>

<script>
import * as echarts from "echarts";
import { queryOrderCount } from "@/api/homePage/order.js";
export default {
  data() {
    return {
      orderPieChart: null,
    };
  },
  mounted() {
    this.orderPieChart = echarts.init(this.$refs.pieChartOne);

    this.getData();
  },
  beforeDestroy() {
    this.orderPieChart.dispose();
    this.orderPieChart = null;
  },
  methods: {
    async getData() {
      const res = await queryOrderCount({});
      console.log("res: ", res);

      let bgColor = "#fff";
      let color = ["#e89e42", "#4aa4ea"];
      let echartData = [
        {
          name: "需求订单",
          value: res.data.demandCount,
        },
        {
          name: "handle品订单",
          value: res.data.handleCount,
        },
      ];

      let formatNumber = function (num) {
        let reg = /(?=(\B)(\d{3})+$)/g;
        return num.toString().replace(reg, ",");
      };
      let total = echartData.reduce((a, b) => {
        return a + b.value * 1;
      }, 0);

      const option = {
        title: {
          text: "累计订单数量",
          x: "center",
          y: "7px",
          textStyle: {
            color: "#555555",
            fontSize: 16,
          },
          textAlign: "left",
        },
        backgroundColor: bgColor,
        color: color,
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            radius: ["45%", "60%"],
            center: ["50%", "50%"],
            data: echartData,
            hoverAnimation: false,
            itemStyle: {
              normal: {
                borderColor: bgColor,
                borderWidth: 2,
              },
            },
            labelLine: {
              normal: {
                length: 20,
                length2: 100,
                lineStyle: {
                  color: ["#62a3e3", "#e89e42"],
                },
              },
            },
            label: {
              normal: {
                formatter: (params) => {
                  return (
                    "{icon|●}{name|" +
                    params.name +
                    "}{value|" +
                    formatNumber(params.value) +
                    "}"
                  );
                },
                padding: [0, -100, 25, -100],
                rich: {
                  icon: {
                    fontSize: 16,
                  },
                  name: {
                    fontSize: 14,
                    padding: [0, 10, 0, 4],
                    color: "#666666",
                  },
                  value: {
                    fontSize: 18,
                    fontWeight: "bold",
                    color: "#333333",
                  },
                },
              },
            },
          },
        ],
      };
      this.orderPieChart.setOption(option);
    },
  },
};
</script>

<style lang="scss" scoped>
.bar-chart-one {
  width: 100%;
  height: 400px;
}
</style>