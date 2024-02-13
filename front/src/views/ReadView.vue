<script setup lang="ts">
import {defineProps, onMounted, ref} from 'vue'
import axios from "axios";

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

onMounted(() => {
  axios.get(`/my-backend/api/v1/blogs/${props.blogId}`).then((response) => {
     blog.value = response.data.data;
  })
})
</script>

<template>
  <h2>
    {{blog.title}}
  </h2>

  <div>
    {{blog.content}}
  </div>
</template>