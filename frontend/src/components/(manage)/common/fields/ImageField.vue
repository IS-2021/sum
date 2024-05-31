<script setup lang="ts">
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import { ref } from 'vue';
import { ArrowRightIcon } from 'lucide-vue-next';
import { Label } from '@/components/ui/label';
import type { ImageChangeEvent } from '@/components/(manage)/common/fields/types';

const emits = defineEmits<{
  (e: 'onChange', image: ImageChangeEvent): void;
}>();

type FileInput = {
  error: string;
  url: string;
  file: File | null;
};

const defaultFileInputValues: FileInput = {
  error: '',
  url: '',
  file: null,
};

const fileInputRef = ref<HTMLInputElement>();
const fileInput = ref<FileInput>(defaultFileInputValues);

function getImageFromFiles(files: FileList | null) {
  if (!files) {
    throw new Error('No file selected');
  }

  if (files.length !== 1) {
    throw new Error('Only one file is allowed');
  }
  const file = files[0];

  if (file.type !== 'image/jpeg') {
    throw new Error('Only JPEG files are allowed');
  }

  if (file.size > 1 * 1024 * 1024) {
    throw new Error('File size must be less than 1MB');
  }

  return file;
}

const handleFileChange = (event: Event) => {
  if (!(event.target instanceof HTMLInputElement)) {
    return;
  }

  const files = (event.target as HTMLInputElement).files;

  let imageFile: File;
  try {
    imageFile = getImageFromFiles(files);
  } catch (error) {
    event.target.value = '';

    URL.revokeObjectURL(fileInput.value.url);
    fileInput.value = defaultFileInputValues;
    if (error instanceof Error) {
      fileInput.value.error = error.message;
    }

    return;
  }

  fileInput.value = defaultFileInputValues;
  fileInput.value.url = URL.createObjectURL(imageFile);
  fileInput.value.file = imageFile;
};

async function handleSaveRequest() {
  if (!fileInput.value.file) {
    return;
  }

  emits('onChange', {
    image: fileInput.value.file,
    previewUrl: fileInput.value.url,
  });
}
</script>

<template>
  <Label class="mb-4 block space-y-1">
    <Input type="file" ref="fileInputRef" accept="image/jpeg" @change="handleFileChange" />
    <span class="block text-sm text-neutral-500">JPG, max 1MB</span>
  </Label>

  <p v-if="fileInput.error" class="mb-4 text-red-500">{{ fileInput.error }}</p>

  <AspectRatio v-if="fileInput.url" :ratio="16 / 9" class="overflow-clip rounded-md">
    <img aria-hidden="true" :src="fileInput.url" alt="" class="h-full w-full object-cover" />
  </AspectRatio>

  <div class="mt-6 flex gap-2">
    <Button @click="handleSaveRequest" :disabled="!fileInput.file"> Save </Button>
    <Button variant="outline" as-child>
      <RouterLink to="/manage/dashboard">
        Skip for now <ArrowRightIcon class="ml-2 h-4 w-4" />
      </RouterLink>
    </Button>
  </div>
</template>
