%Quiz Recommendation Systems:
% alpha = 2;
% Table =[ 
% 1	0	1	0	1	2 * alpha
% 1	1	0	0	1	6 * alpha
% 0	1	0	1	0	2 * alpha];
% 
% for i=1:3
%     for j=i+1:3
%     disp(strcat(int2str(i),',',int2str(j)));
%     sum(Table(i,:).*Table(j,:)) / (norm(Table(i,:))*norm(Table(j,:)))
%     end
% end

% table =[1	1	0	1	0	0	1	0
% 		0	1	1	1	0	0	0   0
%         0	0	0	1	0	1	1	1];
%   JaccardDist = zeros(size(table));
%     
%  for i=1:size(table,2)
%      for j=1:size(table,2)
%          JaccardDist(i,j) = 1 - sum(table(:,i) .* table(:,j)) / (sum(table(:,i) | table(:,j)));
%      end
%  end
%  
 
 table = [ 1 2	3	4	5
    2	3	2	5	3
	5	5	5	3	2];

rowAvgs = sum(transpose(table))/size(table,2);
for i=1:size(table,1)
    table(i,:) = table(i,:) - rowAvgs(i);
end

columnAgs = mean(table);

for i=1:size(table,2)
table(:,i) = table(:,i) - columnAgs(i);
end