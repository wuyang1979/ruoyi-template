<template>
  <div>
    <div ref="catePieChart" class="cate-pie-chart" />
  </div>
</template>

<script>
import * as echarts from "echarts";
import {queryBlockData} from '@/api/homePage/getNum.js'
export default {
  data() {
    return {
      catePie: null,
    };
  },
  mounted() {
    this.catePie = echarts.init(this.$refs.catePieChart);
    this.getData()
    
  },
  beforeDestroy() {
    this.catePie.dispose(), (this.catePie = null);
  },
  methods:{
    async getData(){
      const res=await queryBlockData({})
      const count=[
        { value: res.data.parkCount, name: "精准招商" },
        { value: res.data.appCount, name: "应用服务" },
        { value: res.data.supplyCount, name: "供需对接" },
        { value: res.data.newsCount, name: "新闻动态" },
        { value: res.data.thinkTankCount, name: "行业智库" },];
const option = {
  title: {
    text: "板块内容",
    x: "center",
     textStyle: {
          color: "#555555",
          fontSize: 16,
        },
  },
  tooltip: {
    trigger: "item",
    formatter: "{a} <br/>{b} : {c} ({d}%)",
  },
  color:['#76c681','#5ea0f5','#eba768','#8185e2','#e26a85'],
  series: [
    {
      type: "pie",
      radius: "55%",
      center: ["50%", "60%"],
      label: {
        show: true,
        formatter: "{b}: {c}",
        
      },
      labelLine: {
            normal: {
              length: 15,
              length2: 60,
              lineStyle: {
                color: "#1E1E1E",
              },
            },
          },
      data:count,
      itemStyle: {
        emphasis: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.5)",
        },
      },
    },
  ],
};
    this.catePie.setOption(option)
    }
  }
};
</script>

<style lang="scss" scoped>
.cate-pie-chart {
  width: 100%;
  height: 400px;
}
</style>