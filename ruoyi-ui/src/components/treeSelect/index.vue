<template>
  <el-select :value="valueTitle" :disabled="disabled" :clearable="clearable" @clear="clearHandle">
    <el-option :value="valueTitle" :label="valueTitle">
      <el-tree
        id="tree-option"
        ref="selectTree"
        :accordion="accordion"
        :data="options"
        :props="props"
        :node-key="props.value"
        :default-expanded-keys="defaultExpandedKey"
        @node-click="handleNodeClick"
      ></el-tree>
    </el-option>
  </el-select>
</template>
<script>
// import { getBigDept } from "@/api/system/dept";
export default {
  name: 'TreeSelecter',
  props: {
    /* 配置项 */
    props: {
      type: Object,
      default: () => {
        return {
          value: 'deptId', // ID字段名
          label: 'deptName', // 显示名称
        }
      },
    },
    /* 选项列表数据(树形结构的对象数组) */
    options: {
      type: Array,
      default: () => {
        return []
      },
    },
    /* 初始值 */
    value: {
      type: Number,
      default: () => {
        return 1
      },
    },
    /* 可清空选项 */
    clearable: {
      type: Boolean,
      default: () => {
        return true
      },
    },
    /* 自动收起 */
    accordion: {
      type: Boolean,
      default: () => {
        return false
      },
    },
    disabled: {
      type: Boolean,
      default: () => {
        return false
      },
    },
  },
  data() {
    return {
      valueId: this.value, // 初始值
      valueTitle: '',
      defaultExpandedKey: [],
    }
  },
  mounted() {
    this.initHandle()
  },
  methods: {
    // 初始化值
    initHandle() {
      // alert('this.valueId=' + this.valueId)
      let that = this

      setTimeout(function () {
        if (that.valueId || that.valueId === 0) {
          that.valueTitle = that.$refs.selectTree.getNode(that.valueId).data[
            that.props.label
          ] // 初始化显示
          that.$refs.selectTree.setCurrentKey(that.valueId) // 设置默认选中
          that.defaultExpandedKey = [that.valueId] // 设置默认展开
        } else {
          that.valueTitle = '' // 初始化显示
          that.$refs.selectTree.setCurrentKey(null) // 设置默认选中
        }
      }, 200)
      this.$nextTick(() => {
        let scrollWrap = document.querySelectorAll(
          '.el-scrollbar .el-select-dropdown__wrap'
        )[0]
        let scrollBar = document.querySelectorAll(
          '.el-scrollbar .el-scrollbar__bar'
        )
        scrollWrap.style.cssText =
          'margin: 0px; max-height: none; overflow: hidden;'
        scrollBar.forEach((ele) => (ele.style.width = 0))
      })
    },
    // loadNode(node, resolve) {
    // 	let level = node.level
    //   if (level === 0) {
    //     const city = [{
    // 			deptId: 1,
    // 			deptName: '国家'
    // 		}];
    // 		resolve(city);
    // 		return level = 1
    //   } else if (level === 1) {
    // 		getBigDept(1).then(res => {
    // 			resolve(res.data);
    // 		})
    // 		return level = 2
    //   } else if (level === 2) {
    // 		getBigDept(node.data.deptId).then(res => {
    // 			resolve(res.data);
    // 		})
    // 		return level = 3
    // 	} else if (level === 3) {
    // 		getBigDept(node.data.deptId).then(res => {
    // 			resolve(res.data);
    // 		})
    // 		return level = 4
    // 	} else if (level === 4) {
    // 		getBigDept(node.data.deptId).then(res => {
    // 			resolve(res.data);
    // 		})
    // 		return level = 5
    // 	} else if (level === 5) {
    // 		getBigDept(node.data.deptId).then(res => {
    // 			resolve(res.data);
    // 		})
    // 		return level = 5
    // 	}
    // },
    // 切换选项
    handleNodeClick(node) {
      this.valueTitle = node[this.props.label]
      this.valueId = node[this.props.value]
      //this.$emit('getValue',this.valueId)
      this.$emit('input', this.valueId)
      this.defaultExpandedKey = []
    },
    // 清除选中
    clearHandle() {
      this.valueTitle = ''
      this.valueId = null
      this.defaultExpandedKey = []
      this.clearSelected()
      //this.$emit('getValue',null)
      this.$emit('input', null)
    },
    /* 清空选中样式 */
    clearSelected() {
      let allNode = document.querySelectorAll('#tree-option .el-tree-node')
      allNode.forEach((element) => element.classList.remove('is-current'))
    },
  },
  watch: {
    value() {
      this.valueId = this.value
      this.initHandle()
    },
  },
}
</script>
<style scoped>
.el-scrollbar .el-scrollbar__view .el-select-dropdown__item {
  height: auto;
  max-height: 274px;
  padding: 0;
  overflow: hidden;
  overflow-y: auto;
}
.el-select-dropdown__item.selected {
  font-weight: normal;
}
ul li >>> .el-tree .el-tree-node__content {
  height: auto;
  padding: 0 20px;
}
.el-tree-node__label {
  font-weight: normal;
}
.el-tree >>> .is-current .el-tree-node__label {
  color: #409eff;
  font-weight: 700;
}
.el-tree >>> .is-current .el-tree-node__children .el-tree-node__label {
  color: #606266;
  font-weight: normal;
}
</style>
