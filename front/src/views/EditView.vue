<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {defineProps, ref} from "vue";
  import axios from "axios";

  const  router = useRouter()

  const props = defineProps({
    blogId : {
      type : [Number, String],
      require : true
    }
  })

  const blog = ref({
    id : 0,
    title : "",
    content : ""
  });

  axios.get(`/my-backend/api/v1/blogs/${props.blogId}`).then((response) => {
    console.log(response)
    blog.value = response.data.data;
  });

  const edit = () => {
    axios.patch(`/my-backend/api/v1/blogs/${props.blogId}`, blog.value).then((response) => {
      console.log(response)
      router.replace({name: "home"})
    });

  }

</script>

<template>
  <div class="mt-2">
    <el-input type="text" placeholder="제목을 입력해주세요." v-model="blog.title"/>
  </div>

  <div>
    <div class="mt-2">
    <el-input type="textarea" rows="15" v-model="blog.content"></el-input>
    </div>

    <div class="mt-2">
      <el-button type="warning" @click="edit()">수정 완료!</el-button>
    </div>
  </div>
</template>

<style>

</style>