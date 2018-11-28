FROM node:latest

# Install dependencies
RUN mkdir /gui
WORKDIR gui
ADD gui /gui
RUN npm install

# Transpile Typescript
RUN npm install -g typescript
RUN tsc

# NPM start
CMD npm run lite
