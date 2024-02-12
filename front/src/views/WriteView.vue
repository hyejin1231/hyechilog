<script setup lang="ts">
  import {ref} from "vue";
  import axios from "axios";

  const title = ref("")
  const content = ref("")

  const write = function () {
    console.log(title.value + " / " + content.value)
    axios.post("/my-backend/api/v1/blogs/new", {
      title:title.value, content: content.value
    })
  };


</script>

<template>
  <div class="mt-2">
    <el-input type="text" placeholder="제목을 입력해주세요." v-model="title"/>
  </div>

  <div>
    <div class="mt-2">
    <el-input type="textarea" rows="15" v-model="content"></el-input>
    </div>

    <div class="mt-2">
      <el-button @click="write()">등록 완료!</el-button>
    </div>
  </div>
</template>

<style>

</style>