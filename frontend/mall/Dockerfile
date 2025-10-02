# 빌드 스테이지
FROM node:18-alpine AS build

WORKDIR /app

# package.json 복사 및 의존성 설치
COPY package*.json ./
RUN npm install

# 소스 코드 복사 및 빌드
COPY . .
RUN npm run build

# 프로덕션 스테이지 (Nginx)
FROM nginx:alpine

# 빌드된 파일을 nginx로 복사
COPY --from=build /app/build /usr/share/nginx/html

# Nginx 설정 (SPA용 - 선택사항)
RUN echo 'server { \
    listen 80; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]