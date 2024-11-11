import apiClient from '../../../../apis/apiClient';

export const getApiSpec = async(projectId:string) => {
  try{
    const response = await apiClient.get(`/projects/${projectId}/api-docs`);
    console.log(response.data);
    return response.data;
  } catch (error){
    console.log(error);
    throw error;
  }
}