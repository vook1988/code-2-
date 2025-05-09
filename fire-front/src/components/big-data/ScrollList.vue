<template>
    <vue-seamless-scroll :data="listData" :class-option="classOption">
      <ul>
        <li v-for="(item, index) in listData" :key="index">{{ item }}</li>
      </ul>
    </vue-seamless-scroll>
  </template>
  
  <script>
  import VueSeamlessScroll from 'vue-seamless-scroll';
  
  export default {
    components: {
      VueSeamlessScroll
    },
    props: {
      listData: {
        type: Array,
        default: () => []
      }
    },
    data() {
      return {
        classOption: {
          step: 0.2,
          limitMoveNum: 5,
          hoverStop: true,
          direction: 1,
          openWatch: true,
          singleHeight: 0,
          singleWidth: 0,
          waitTime: 1000
        }
      };
    }
  };
  </script>