# Function to echo a cool header
echo_header() {
  local header_text=$1
  local border="*****************************************************************"
  local purple='\033[0;35m'
    local no_color='\033[0m'

  echo ""
  echo -e "${purple}${border}${no_color}"
  echo -e "${purple}*  $header_text  *${no_color}"
  echo -e "${purple}${border}${no_color}"
  echo ""
}