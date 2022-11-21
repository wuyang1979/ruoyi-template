<template>
  <div class="map">
    <div class="other" v-if="!readOnly">
      <div class="other-head">{{searchAddress}}</div>
      <div class="other-content">
        <div>
          <span>经度：</span>
          <el-input v-model="coordinate.lng" disabled></el-input>
        </div>
        <div>
          <span>纬度：</span>
          <el-input v-model="coordinate.lat" disabled></el-input>
        </div>
        <div>
          <el-button type="primary" @click="sure">确定</el-button>
        </div>
      </div>
    </div>
    <div class="bm-box">
      <el-autocomplete
        v-if="!readOnly"
        v-model.trim="searchAddress"
        :fetch-suggestions="querySearch"
        placeholder="请输入详细地址"
        ref="autocomplete"
        class="bm-search"
        :trigger-on-focus="false"
        @select="handleSelect"
      />
      <baidu-map
        class="bm-view"
        :center="mapCenter"
        :zoom="mapZoom"
        @click="paintPolyline"
        :scroll-wheel-zoom="true"
        ak="ZjXLPl1FyNI0Ib3fctqR58fCRs1zrp0l"
        @ready="handlerBMap"
      >
        <bm-marker :position="coordinate"></bm-marker>
      </baidu-map>
    </div>
  </div>
</template>

<script>
import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
import BmMarker from 'vue-baidu-map/components/overlays/Marker.vue'
export default {
  name: 'mapView',
  components: {
    BaiduMap,
    BmMarker,
  },
  props: {
    // 地址
    address: {
      type: String,
      default: () => {
        return ''
      },
    },
    // 经纬度
    latlng: {
      type: Object,
      default: () => {
        return {}
      },
    },
    // 只读
    readOnly: {
      type: Boolean,
      default: () => {
        return false
      },
    },
  },
  data() {
    return {
      coordinate: this.latlng,
      searchAddress: this.address,
      mapZoom: 15,
      mapCenter: { lng: 116.403414, lat: 39.910924 },
    }
  },
  mounted() {},
  methods: {
    async handlerBMap({ BMap, map }) {
      this.BMap = BMap
      this.map = map
      map.disableDoubleClickZoom() //禁止双击地图放大
      //禁止双指缩放disablePinchToZoom()
      // map.disableDoubleClickZoom() //禁止缩放
      //map.enableScrollWheelZoom(false); //开启鼠标滚轮缩放
      // map.disableDragging() //禁止拖拽
      if (this.coordinate && this.coordinate.lng) {
        this.mapCenter.lng = this.coordinate.lng
        this.mapCenter.lat = this.coordinate.lat
        this.mapZoom = 15
        map.addOverlay(new this.BMap.Marker(this.coordinate))
      } else {
        this.mapCenter.lng = this.coordinate.lng || 116.403414
        this.mapCenter.lat = this.coordinate.lat || 39.910924
        this.mapZoom = 15
      }
      if (this.address) {
        this.$refs.autocomplete.activated = true
        this.$refs.autocomplete.getData(this.address)
      }
    },
    querySearch(queryString, cb) {
      var that = this
      var myGeo = new this.BMap.Geocoder()
      myGeo.getPoint(
        queryString,
        function (point) {
          if (point) {
            that.coordinate = point
            that.makerCenter(point)
          } else {
            that.coordinate = null
          }
        },
        this.locationCity
      )
      var options = {
        onSearchComplete: function (results) {
          if (local.getStatus() === 0) {
            // 默认选择第一条
            let first = results.getPoi(0)
            that.searchAddress = first.address + first.title
            // 判断状态是否正确
            var s = []
            for (var i = 0; i < results.getCurrentNumPois(); i++) {
              var x = results.getPoi(i)
              var item = { value: x.address + x.title, point: x.point }
              s.push(item)
              cb(s)
            }
          } else {
            cb()
          }
        },
      }
      var local = new this.BMap.LocalSearch(this.map, options)
      local.search(queryString)
    },
    paintPolyline(item) {
      // var { point } = item
      // this.flag = true
      // this.makerCenter(point)
    },
    handleSelect(item) {
      var { point } = item
      this.coordinate = point
      this.makerCenter(point)
    },
    makerCenter(point) {
      if (this.map) {
        this.map.clearOverlays()
        this.map.addOverlay(new this.BMap.Marker(point))
        this.mapCenter.lng = point.lng
        this.mapZoom = 15
        this.mapCenter = { lng: point.lng, lat: point.lat }
      }
    },
    sure() {
      this.$emit('setLatlng', {
        lat: this.coordinate.lat,
        lng: this.coordinate.lng,
        address: this.searchAddress,
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.map {
  display: flex;
  flex-direction: column;
  .other {
    margin-bottom: 15px;
    .other-head {
      margin-bottom: 10px;
    }
    .other-content {
      display: flex;
      div:not(:last-child) {
        margin-right: 20px;
        display: flex;
        align-items: center;
        > span {
          width: 60px;
        }
      }
    }
  }
  .bm-box {
    width: 100%;
    height: 100%;
    position: relative;
    .bm-search {
      width: 300px;
      position: absolute;
      top: 20px;
      left: 20px;
      z-index: 99;
    }
    .bm-view {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
