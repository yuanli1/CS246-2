% 
% costC1 =[];
% costC2 = [];
% centroids = [];
% for i=1:20
%     cd(strcat('outputc1_', num2str(i-1),'\'))
%     fid = fopen('part-r-00000');
%     tline = fgets(fid);
%     while ischar(tline)
%         if(i == 20)&&(isempty(strfind(tline,'Cost')))
%             splitted = strsplit(tline,' ');
%             centroid =[];
%             for j=1:size(splitted,2)
%                 centroid = [centroid str2num(char(splitted(1,j)))];
%             end
%             centroids = [centroids;centroid];
%         end
%         if(strfind(tline,'Cost'))
%             splitted = strsplit(tline,'\t');
%             costC1 = [costC1 str2num(char(splitted(2)))];
%             break;
%         end
%          tline = fgets(fid);
%     end
%     
%     fclose(fid);
%     cd ..
%     
%     cd(strcat('outputc2_', num2str(i-1),'\'))
%     fid = fopen('part-r-00000');
%     tline = fgets(fid);
%     while ischar(tline)
%         %disp(tline)
%         %if(tline
%         tline = fgets(fid);
%         if(strfind(tline,'Cost'))
%             splitted = strsplit(tline,'\t');
%             costC2 = [costC2 str2num(char(splitted(2)))];
%             break;
%         end
%     end
%     
%     fclose(fid);
%     cd ..
% end
%%%%%%%%%%%%%%%%%%%%%%%%%%%Loading vocab:%%%%%%%%%%%%%%%%%%%%%%%%%
% vocab =[];
% fid = fopen('vocab.txt');
%     tline = fgets(fid);
%     while ischar(tline)
%          vocab = [vocab;cellstr(tline)];
%          tline = fgets(fid);
%     end
%  fclose(fid);
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 %%%%%%%%%% Finding the cluster for a document:******
%  minDistance = +inf;
%  minIndex = 0;
%  
%  for i=1:size(centroids,1)
%      
%      currentDistance=    sum((centroids(i,:)-doc2).^2);
%      if(currentDistance < minDistance)
%          minDistance = currentDistance;
%          minIndex= i;
%      end
%  end
%  [sortedVal,sortedIndex]=sort(centroids(minIndex,:),'descend');
%  vocab(sortedIndex(1:10))

%%%%%%%%%%%%%%%%%%%% Q4c: Cost percentage change: %%%%%%%%%%%%%%%%%%%%%%%%
disp('Percentage Change C1:');
(costC1(1,10)-costC1(1,1)) / costC1(1,1) * 100

disp('Percentage Change C2:');
(costC2(1,10)-costC2(1,1)) / costC2(1,1) * 100
 