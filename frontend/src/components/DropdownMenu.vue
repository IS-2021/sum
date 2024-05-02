<script setup lang="ts">
import { Menu } from 'lucide-vue-next';
import { ref } from 'vue'
import {
  DropdownMenuArrow,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuPortal,
  DropdownMenuRoot,
  DropdownMenuTrigger,
} from 'radix-vue'

const toggleState = ref(false)

</script>

<template>
  <DropdownMenuRoot v-model:open="toggleState">
    <DropdownMenuTrigger
      class="sm:hidden rounded-full w-[35px] h-[35px] inline-flex items-center justify-center text-grass11 outline-none"
      aria-label="Customise options">
      <Menu />
    </DropdownMenuTrigger>

    <DropdownMenuPortal>
      <DropdownMenuContent
        class="sm:hidden min-w-[300px] h-screen outline-none bg-neutral-800 rounded-md p-4 shadow-[0px_10px_38px_-10px_rgba(22,_23,_24,_0.35),_0px_10px_20px_-15px_rgba(22,_23,_24,_0.2)] will-change-[opacity,transform] data-[side=top]:animate-slideDownAndFade data-[side=right]:animate-slideLeftAndFade data-[side=bottom]:animate-slideUpAndFade data-[side=left]:animate-slideRightAndFade"
        :side-offset="5">
        <DropdownMenuItem value="New Tab"
          class="group leading-none text-grass11 rounded-[3px] flex items-center h-[38px] px-[5px] relative pl-5 pt-5 select-none outline-none data-[disabled]:text-mauve8 data-[disabled]:pointer-events-none data-[highlighted]:bg-green9 data-[highlighted]:text-green1">
          <RouterLink to="/">Home</RouterLink>
        </DropdownMenuItem>
        <DropdownMenuItem value="New Tab"
          class="group leading-none text-grass11 rounded-[3px] flex items-center h-[25px] px-[5px] relative pl-5 pt-10 select-none outline-none data-[disabled]:text-mauve8 data-[disabled]:pointer-events-none data-[highlighted]:bg-green9 data-[highlighted]:text-green1">
          <RouterLink to="/restaurants">Restaurants</RouterLink>
        </DropdownMenuItem>
        <DropdownMenuArrow class="fill-white" />
      </DropdownMenuContent>
    </DropdownMenuPortal>
  </DropdownMenuRoot>
</template>
