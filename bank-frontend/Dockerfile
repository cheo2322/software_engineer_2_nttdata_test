FROM node:20

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

ARG VITE_BANK_BACKEND_BASE_URL
ENV VITE_BANK_BACKEND_BASE_URL=$VITE_BANK_BACKEND_BASE_URL

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host"]