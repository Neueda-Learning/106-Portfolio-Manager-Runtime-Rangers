#-----------nginx stage----------
FROM nginx:alpine

#COPY index_active.html /usr/share/nginx/html/\
# remove default website
RUN rm -rf /usr/share/nginx/html/*

# copy react build files to nginx
COPY --from=build frontend/dist /usr/share/nginx/html
# Copy all website files
# COPY . /usr/share/nginx/html/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
